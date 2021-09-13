# Copyright (C) 2016 David Brown <david.brown@linaro.org>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "OPTEE test"
HOMEPAGE = "http://www.optee.org/"

# TODO: Get the license files into the repo, and refer to them here.
LICENSE = "BSD"

DEPENDS = "optee-os optee-client"

SRC_URI = "git://github.com/OP-TEE/optee_test.git \
"
SRCREV = "f461e1d47fcc82eaa67508a3d796c11b7d26656e"
PR = "r0"
PV = "3.9.0+git${SRCPV}"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=daa2bcccc666345ab8940aab1315a4fa"

inherit python3native

DEPENDS += "python3-pycryptodomex-native"

S = "${WORKDIR}/git"

do_compile () {
    export TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta
    export TEEC_EXPORT=${STAGING_DIR_HOST}/usr

    export OPTEE_CLIENT_EXPORT=${STAGING_DIR_HOST}/usr
    oe_runmake V=1 CROSS_COMPILE_HOST=${HOST_PREFIX} \
        CROSS_COMPILE_TA=${HOST_PREFIX}
}

do_install () {
    install -d ${D}/usr/bin
    install -d ${D}/lib/optee_armtz

    install ${S}/out/xtest/xtest ${D}/usr/bin/

    find ${S}/out/ta -name '*.ta' | while read name; do
        install -m 444 $name ${D}/lib/optee_armtz/
    done
}

FILES_${PN} = "/usr/bin/ /lib/optee_armtz/"

INHIBIT_PACKAGE_STRIP = "1"
