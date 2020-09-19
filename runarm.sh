#! /bin/bash

root=build/tmp-glibc/sysroots/x86_64-linux
deploy=build/tmp-glibc/deploy/images/qemu-optee32

./build/tmp-glibc/work/x86_64-linux/qemu-native/2.7.0+gitAUTOINC+173ff58580-r0/build/arm-softmmu/qemu-system-arm \
	-s -S \
	-nographic \
	-machine virt,secure=on \
	-cpu cortex-a15 \
	-bios $deploy/optee-image-qemu-optee32.bios \
	-serial tcp:localhost:54320 -serial tcp:localhost:54321 \
	-m 1058 \
	-netdev user,id=unet,hostfwd=tcp::5001-:22,hostfwd=tcp::2159-:2159 \
	-device virtio-net-device,netdev=unet \
	-device virtio-scsi-device,id=scsi \
	-drive file=${deploy}/optee-image-qemu-optee32.ext4,id=rootimg,if=none,format=raw \
	-device scsi-hd,drive=rootimg
