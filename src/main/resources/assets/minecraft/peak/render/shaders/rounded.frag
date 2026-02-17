#version 120

uniform vec2 location;
uniform vec2 rectSize;
uniform vec4 color;
uniform float radius;

float roundedBoxSDF(vec2 p, vec2 b, float r) {
    vec2 q = abs(p) - b + r;
    return length(max(q, 0.0)) + min(max(q.x, q.y), 0.0) - r;
}

void main() {
    vec2 p = gl_FragCoord.xy - (location + rectSize / 2.0);

    float d = roundedBoxSDF(p, rectSize / 2.0, radius);

    float alpha = color.a * (1.0 - smoothstep(-0.5, 0.5, d));

    gl_FragColor = vec4(color.rgb, alpha);
}