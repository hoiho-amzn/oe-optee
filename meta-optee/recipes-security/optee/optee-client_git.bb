# Copyright (C) 2016 David Brown <david.brown@linaro.org>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "OPTEE Client libs"
HOMEPAGE = "http://www.optee.org/"
LICENSE = "BSD | LGPLv2"

SRC_URI = " \
    git://github.com/OP-TEE/optee_client.git \
    file://tee-supplicant.init \
"
SRCREV = "06e1b32f6a7028e039c625b07cfc25fda0c17d53"
PR = "r0"
PV = "3.14.0+git${SRCPV}"
LIC_FILES_CHKSUM = "file://LICENSE;md5=69663ab153298557a59c67a60a743e5b"

S = "${WORKDIR}/git"
B = "${WORKDIR}/git/out"

inherit update-rc.d
inherit python3native

# Note that the Makefiles for optee-client are broken, and O= must be
# a relative path.  To make this work, just don't set anything, and
# always use ${S}/out, instead of ${B}.  This breaks 'devtool'.

do_compile () {
    oe_runmake -C ${S} EXPORT_DIR=${D} build-libteec
    oe_runmake -C ${S} EXPORT_DIR=${D} build-libckteec
    oe_runmake -C ${S} EXPORT_DIR=${D} build-tee-supplicant
}

do_install () {
    # Install into scratch directory
    mkdir -p ${WORKDIR}/scratch/
    oe_runmake -C ${S} EXPORT_DIR=${WORKDIR}/scratch/ install

    # Install .so and tee-supplicant
    install -d ${D}${libdir}
    oe_soinstall ${WORKDIR}/scratch/usr/lib/libteec.so.1.0.0 ${D}${libdir}
    oe_soinstall ${WORKDIR}/scratch/usr/lib/libckteec.so.0.1.0 ${D}${libdir}
    install -d ${D}${sbindir}
    install -m 755 ${WORKDIR}/scratch/usr/sbin/tee-supplicant ${D}${sbindir}/tee-supplicant

    # Install headers
    install -d ${D}/usr/include/
    install -m 644 ${WORKDIR}/scratch/usr/include/*.h ${D}/usr/include/

    # Startup script.
    install -d ${D}/etc/init.d
    install -m 755 ${WORKDIR}/tee-supplicant.init ${D}/etc/init.d/tee-supplicant
}

INITSCRIPT_NAME = "tee-supplicant"

PACKAGES += "tee-supplicant"
FILES_${PN} = "${libdir}/libteec* ${libdir}/libckteec*"
FILES_tee-supplicant = "${sbindir}/tee-supplicant"
FILES_${PN} += "${sysconfdir}/init.d/tee-supplicant"

# Debugging
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
