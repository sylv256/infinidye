#version 330

const uint frp_quadMaterialId;

out vec2 ftm_texCoord;

out vec4 frp_vertColor;

out vec4 infd_vertColor;

void frp_inputVertex() {
    infd_vertColor = (frp_quadMaterialId == FRP_MATERIAL_ID) ? vec4(ftm_texCoord, frp_vertColor.a, 1.0) : vec4(1.0);
    frp_vertColor = (frp_quadMaterialId == FRP_MATERIAL_ID) ? vec4(frp_vertColor.rgb, 1.0) : frp_vertColor;
}
