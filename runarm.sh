#!/bin/bash

root=build/tmp-glibc/sysroots-components/x86_64/qemu-system-native
deploy=build/tmp-glibc/deploy/images/qemu-optee32

HOSTFWD=

${root}/usr/bin/qemu-system-arm \
	-nographic \
	-serial tcp:localhost:54320 -serial tcp:localhost:54321 \
	-s -S -machine virt,secure=on -cpu cortex-a15 \
	-object rng-random,filename=/dev/urandom,id=rng0 \
	-device virtio-rng-pci,rng=rng0,max-bytes=1024,period=1000 \
	-netdev user,id=vmnic${HOSTFWD} -device virtio-net-device,netdev=vmnic \
	-m 1057 \
	-bios $deploy/optee-image-qemu-optee32.bios \
	-device virtio-scsi-device,id=scsi \
	-drive file=${deploy}/optee-image-qemu-optee32.ext4,id=rootimg,if=none,format=raw \
	-device scsi-hd,drive=rootimg
