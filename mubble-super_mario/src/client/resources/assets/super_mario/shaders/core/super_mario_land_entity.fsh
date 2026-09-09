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

// The four greens a Game Boy screen can show, from darkest to brightest. Super Mario Land had these and
// nothing else, so they are all the Superball form gets.
const vec3 C1 = vec3(0.059, 0.220, 0.059);
const vec3 C2 = vec3(0.188, 0.384, 0.188);
const vec3 C3 = vec3(0.545, 0.675, 0.059);
const vec3 C4 = vec3(0.608, 0.737, 0.059);

// Unlike the gold and ice palettes, this one does not blend between its stops: a Game Boy picks one of four
// shades per pixel and that hard banding is the whole look of the thing.
vec3 getGameBoyShade(float luma) {
    if (luma < 0.25) return C1;
    if (luma < 0.50) return C2;
    if (luma < 0.75) return C3;
    return C4;
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

    vec3 gameBoyColor = getGameBoyShade(luma);

    color = vec4(gameBoyColor, color.a);

    color *= geometryLight * ColorModulator;

    #ifndef NO_OVERLAY
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    #endif

    #ifndef EMISSIVE
    color *= lightMapColor;
    #endif

    fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}
