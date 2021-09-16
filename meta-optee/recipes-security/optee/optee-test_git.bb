# Copyright (C) 2016 David Brown <david.brown@linaro.org>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "OPTEE test"
HOMEPAGE = "http://www.optee.org/"

# TODO: Get the license files into the repo, and refer to them here.
LICENSE = "BSD"

DEPENDS = "optee-os optee-client"

SRC_URI = "git://github.com/OP-TEE/optee_test.git \
"
SRCREV = "f2eb88affbb7f028561b4fd5cbd049d5d704f741"
PR = "r0"
PV = "3.14.0+git${SRCPV}"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=daa2bcccc666345ab8940aab1315a4fa"

inherit python3native

DEPENDS += "python3-pycryptodomex-native"

S = "${WORKDIR}/git"

EXTRA_OEMAKE_append = " LIBGCC_LOCATE_CFLAGS=--sysroot=${STAGING_DIR_HOST}"

do_compile () {
    export TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta
    export TEEC_EXPORT=${STAGING_DIR_HOST}/usr

    export OPTEE_CLIENT_EXPORT=${STAGING_DIR_HOST}/usr
    oe_runmake V=1 CROSS_COMPILE_HOST=${HOST_PREFIX} \
        CROSS_COMPILE_TA=${HOST_PREFIX}
}

do_install () {
    install -d ${D}${bindir}
    install -d ${D}${base_libdir}/optee_armtz
    install -d ${D}${libdir}/tee-supplicant/plugins/

    install ${S}/out/xtest/xtest ${D}${bindir}

    find ${S}/out/ta -name '*.ta' | while read name; do
        install -m 444 $name ${D}${base_libdir}/optee_armtz/
    done

    install ${S}/out/supp_plugin/*.plugin ${D}${libdir}/tee-supplicant/plugins/
}

FILES_${PN} = "/usr/bin/ /lib/optee_armtz/ /usr/lib/tee-supplicant/plugins/"

INHIBIT_PACKAGE_STRIP = "1"
