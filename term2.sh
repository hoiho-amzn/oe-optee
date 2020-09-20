#!/bin/bash

root=build/tmp-glibc/sysroots-components/x86_64/soc-term-native
deploy=build/tmp/-glibc/deploy/images/qemu-optee32

$root/usr/bin/soc_term 54321
