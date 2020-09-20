#!/bin/bash

root=build/tmp-glibc/sysroots-components/x86_64/qemu-system-native
deploy=build/tmp-glibc/deploy/images/qemu-optee32

HOSTFWD=

${root}/usr/bin/qemu-system-arm \
	-s -S \
	-nographic \
	-machine virt,secure=on \
	-cpu cortex-a15 \
	-bios $deploy/optee-image-qemu-optee32.bios \
	-serial tcp:localhost:54320 -serial tcp:localhost:54321 \
	-m 1058 \
	-netdev user,id=vmnic${HOSTFWD} -device virtio-net-device,netdev=vmnic \
	-device virtio-scsi-device,id=scsi \
	-drive file=${deploy}/optee-image-qemu-optee32.ext4,id=rootimg,if=none,format=raw \
	-device scsi-hd,drive=rootimg
