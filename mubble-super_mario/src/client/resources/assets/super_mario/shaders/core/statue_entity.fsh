#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>

uniform sampler2D Sampler0;

in float sphericalVertexDistance;
in float cylindricalVertexDistance;

#ifdef PER_FACE_LIGHTING
in vec4 vertexPerFaceColorBack;
in vec4 vertexPerFaceColorFront;
#else
in vec4 vertexColor;
#endif

#ifndef EMISSIVE
in vec4 lightMapColor;
#endif

#ifndef NO_OVERLAY
in vec4 overlayColor;
#endif

in vec2 texCoord0;

out vec4 fragColor;

// Carved stone: the luma of the skin is cut into four grays, from the dark of a crevice to the light of a polished edge
const vec3 C1 = vec3(0.30, 0.31, 0.33);
const vec3 C2 = vec3(0.47, 0.48, 0.50);
const vec3 C3 = vec3(0.64, 0.65, 0.67);
const vec3 C4 = vec3(0.80, 0.81, 0.83);

vec3 getStoneGradient(float luma) {
    float val = luma * 3.0;

    if (val < 1.0) return mix(C1, C2, val);
    if (val < 2.0) return mix(C2, C3, val - 1.0);
    return mix(C3, C4, val - 2.0);
}

void main() {
    vec4 color = texture(Sampler0, texCoord0);

    #ifdef ALPHA_CUTOUT
    if (color.a < ALPHA_CUTOUT) {
        discard;
    }
    #endif

    #ifdef PER_FACE_LIGHTING
    vec4 geometryLight = gl_FrontFacing ? vertexPerFaceColorFront : vertexPerFaceColorBack;
    #else
    vec4 geometryLight = vertexColor;
    #endif

    float luma = dot(color.rgb, vec3(0.299, 0.587, 0.114));

    vec3 stoneColor = getStoneGradient(luma);

    color = vec4(stoneColor, color.a);

    color *= geometryLight * ColorModulator;

    #ifndef NO_OVERLAY
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    #endif

    #ifndef EMISSIVE
    color *= lightMapColor;
    #endif

    fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}
