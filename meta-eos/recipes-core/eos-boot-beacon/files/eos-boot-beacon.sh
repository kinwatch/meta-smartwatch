#!/bin/sh
# Phase-1 boot verification beacon. If the kernel cmdline contains "eos-beacon",
# this runs at multi-user.target and reboots the device to the bootloader
# (fastboot) via the misc BCB. The device landing in fastboot therefore PROVES
# the eos rootfs booted all the way to systemd multi-user -- an observable,
# WLAN/USB-independent signal (the 1-bit BCB channel, proven in the initramfs).
# Without the cmdline flag it is a no-op, so the same rootfs boots normally
# (Phase 2 = WiFi/ssh).
grep -qw eos-beacon /proc/cmdline 2>/dev/null || exit 0

logger -t eos-boot-beacon "reached multi-user; writing BCB bootonce-bootloader -> fastboot"
MISC=$(readlink -f /dev/block/by-name/misc 2>/dev/null)
[ -b "$MISC" ] || MISC=/dev/block/by-name/misc
# 32-byte BCB command field: "bootonce-bootloader" + NUL padding.
{ printf 'bootonce-bootloader'; dd if=/dev/zero bs=1 count=13 2>/dev/null; } > /tmp/eos-bcb
dd if=/tmp/eos-bcb of="$MISC" bs=1 count=32 conv=notrunc 2>/dev/null
sync
sleep 1
systemctl --force reboot 2>/dev/null || reboot -f
