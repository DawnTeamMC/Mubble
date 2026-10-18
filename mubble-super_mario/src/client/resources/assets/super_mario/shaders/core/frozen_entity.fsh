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

// Ice gradient (the whole palette of the ice block texture, from darkest to brightest)
const vec3 C1 = vec3(0.525, 0.682, 0.992);
const vec3 C2 = vec3(0.549, 0.702, 0.996);
const vec3 C3 = vec3(0.573, 0.725, 0.996);
const vec3 C4 = vec3(0.631, 0.765, 1.000);
const vec3 C5 = vec3(0.737, 0.831, 1.000);
const vec3 C6 = vec3(0.784, 0.863, 1.000);

vec3 getIceGradient(float luma) {
    float val = luma * 5.0;

    if (val < 1.0) return mix(C1, C2, val);
    if (val < 2.0) return mix(C2, C3, val - 1.0);
    if (val < 3.0) return mix(C3, C4, val - 2.0);
    if (val < 4.0) return mix(C4, C5, val - 3.0);
    return mix(C5, C6, val - 4.0);
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

    vec3 iceColor = getIceGradient(luma);

    color = vec4(iceColor, color.a);

    color *= geometryLight * ColorModulator;

    #ifndef NO_OVERLAY
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    #endif

    #ifndef EMISSIVE
    color *= lightMapColor;
    #endif

    fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}
