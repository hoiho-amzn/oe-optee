# Build a qemu bios image out of the various images.

inherit image_types

COMPRESSIONTYPES += "gz.bios"

# TODO: The kernel, other than this symlink seems to have going
# away.
KERNEL_BIOS ?= "${DEPLOY_DIR_IMAGE}/zImage"

# The rootfs can either be the real rootfs, or set to "/dev/null" to
# include a blank rootfs (and expecting the real rootfs to be
# mountable.
BIOS_INITRD ?= "${IMGDEPLOYDIR}/optee-image-qemu-optee32.cpio.gz"

# If the rootfs is not set, BIOS_ROOT_DEVICE should be set to
# something like "root=/dev/sda" for where the given image will be
# accessed.
BIOS_ROOT_DEVICE ?= ""

IMAGE_CMD_bios () {
    rm -rf "${WORKDIR}/bios-work"
    mkdir "${WORKDIR}/bios-work"
    ${COMPONENTS_DIR}/${BUILD_ARCH}/qemu-bios-native/usr/bin/mkbios.sh \
        V=0 \
        CFLAGS="${CFLAGS} --sysroot=${COMPONENTS_DIR}/${BUILD_ARCH}/optee-os/ -mfloat-abi=${TARGET_FPU}" \
        LDFLAGS="" \
        CROSS_COMPILE=${HOST_PREFIX} \
        O=${WORKDIR}/bios-work \
        libgcc=$(${HOST_PREFIX}gcc ${TOOLCHAIN_OPTIONS} --print-libgcc-file-name) \
        BIOS_NSEC_BLOB=${KERNEL_BIOS} \
        BIOS_NSEC_ROOTFS=${BIOS_INITRD} \
        BIOS_SECURE_BLOB=${COMPONENTS_DIR}/${TUNE_PKGARCH}/optee-os/lib/firmware/tee.bin \
        BIOS_ROOT_DEVICE="${BIOS_ROOT_DEVICE}" \
        PLATFORM_FLAVOR=virt
    cp ${WORKDIR}/bios-work/bios.bin ${IMGDEPLOYDIR}/${IMAGE_NAME}.rootfs.bios
}

IMAGE_TYPEDEP_bios = "cpio.gz"
do_image_bios[depends] += "optee-os:do_install qemu-bios-native:do_install virtual/kernel:do_install"

IMAGE_TYPES += "bios"
