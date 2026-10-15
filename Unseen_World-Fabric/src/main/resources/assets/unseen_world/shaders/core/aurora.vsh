#version 150

in vec3 Position;
in vec4 Color;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform float GameTime;

out vec4 vertexColor;

const float TAU = 6.28318530718;
const float COLUMNS = 240.0;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    float azimuth = atan(Position.x, Position.z);
    float column = mod(floor(azimuth / TAU * COLUMNS + 0.5), COLUMNS);
    float seed = fract(sin(column * 12.9898) * 43758.5453);
    float wave = 0.5 + 0.5 * sin(TAU * GameTime * 120.0 - azimuth * 2.0);
    float flicker = 0.5 + 0.5 * sin(TAU * (GameTime * 300.0 + seed));
    float pulse = mix(0.45, 1.0, wave) * mix(0.7, 1.0, flicker);
    vertexColor = vec4(Color.rgb, Color.a * pulse);
}
