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

// Gold gradient (taken from golden dandelion texture, from darkest to brightest)
const vec3 C1 = vec3(0.996, 1.000, 0.902);
const vec3 C2 = vec3(0.965, 0.973, 0.573);
const vec3 C3 = vec3(0.918, 0.933, 0.341);
const vec3 C4 = vec3(0.925, 0.796, 0.271);
const vec3 C5 = vec3(0.859, 0.635, 0.075);
const vec3 C6 = vec3(0.698, 0.392, 0.067);
const vec3 C7 = vec3(0.569, 0.267, 0.035);
const vec3 C8 = vec3(0.459, 0.157, 0.008);
const vec3 C9 = vec3(0.329, 0.071, 0.035);

vec3 getGoldGradient(float luma) {
    float val = luma * 8.0;

    if (val < 1.0) return mix(C9, C8, val);
    if (val < 2.0) return mix(C8, C7, val - 1.0);
    if (val < 3.0) return mix(C7, C6, val - 2.0);
    if (val < 4.0) return mix(C6, C5, val - 3.0);
    if (val < 5.0) return mix(C5, C4, val - 4.0);
    if (val < 6.0) return mix(C4, C3, val - 5.0);
    if (val < 7.0) return mix(C3, C2, val - 6.0);
    return mix(C2, C1, val - 7.0);
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

    vec3 goldColor = getGoldGradient(luma);

    color = vec4(goldColor, color.a);

    color *= geometryLight * ColorModulator;

    #ifndef NO_OVERLAY
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    #endif

    #ifndef EMISSIVE
    color *= lightMapColor;
    #endif

    fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}