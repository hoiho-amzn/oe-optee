# Copyright (C) 2016 David Brown <davidb@davidb.org>
# Released under the MIT license (see COPYING.MIT for the terms)

require linux-optee.inc

SRCREV = "ea89b3a28f6a28c4c158ca3d64c2df4dd68321ed"
PV = "5.4+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;branch=v5.4/standard/arm-versatile-926ejs \
           file://0001-tee-add-support-for-session-s-client-UUID-generation.patch \
           file://0002-tee-optee-Add-support-for-session-login-client-UUID-.patch \
           "

SRC_URI_append_qemu-optee32   = " file://qemu.conf"
SRC_URI_append_qemu-optee64   = " file://qemu.conf"
SRC_URI_append_fvp-optee64    = " file://fvp.conf"
SRC_URI_append_hikey-optee64 = " file://hikey.conf"
SRC_URI_append_hikey-optee64 = " file://usb_net_dm9601.conf"
SRC_URI_append_hikey-optee64 = " file://ftrace.conf"

DESCRIPTION = "Kernel for optee, blah blah blah"

S = "${WORKDIR}/git"

KERNEL_DEVICETREE_fvp-optee64 = "arm/foundation-v8.dtb"
KERNEL_DEVICETREE_hikey-optee64 = "hisilicon/hi6220-hikey.dtb"
