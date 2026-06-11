#version 330

const uint frp_quadMaterialId;

const in vec2 frp_texCoord;

const in vec4 frp_vertColor;

out vec4 frp_fragColor;

in vec4 infd_vertColor;

void frp_inputFragment() {
    frp_fragColor = (frp_quadMaterialId == FRP_MATERIAL_ID) ? 0.25 * frp_fragColor + 0.75 * ftm_vertAo * infd_vertColor : frp_fragColor;
}
