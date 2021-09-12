#!/bin/bash

root=$(readlink -f build/tmp-glibc/sysroots-components/x86_64)
deploy=build/tmp-glibc/deploy/images/qemu-optee32
QEMU=${root}/qemu-system-native/usr/bin/qemu-system-arm
LD_LIBRARY_PATH="${root}/glib-2.0-native/usr/lib/:${root}/libpcre-native/usr/lib"

HOSTFWD=

# Add TEE serial: -serial tcp:localhost:54321
#
# Start in monitor mode (c to start CPU): -S

cd ${deploy} && LD_LIBRARY_PATH=${LD_LIBRARY_PATH} ${QEMU} \
	-nographic \
	-s -machine virt,secure=on -cpu cortex-a15 \
	-object rng-random,filename=/dev/urandom,id=rng0 \
	-device virtio-rng-pci,rng=rng0,max-bytes=1024,period=1000 \
	-netdev user,id=vmnic${HOSTFWD} -device virtio-net-device,netdev=vmnic \
	-m 1057 \
	-bios optee-image-qemu-optee32.bios \
	-d unimp -semihosting-config enable,target=native \
	-device virtio-scsi-device,id=scsi \
	-drive file=optee-image-qemu-optee32.ext4,id=rootimg,if=none,format=raw \
	-device scsi-hd,drive=rootimg
