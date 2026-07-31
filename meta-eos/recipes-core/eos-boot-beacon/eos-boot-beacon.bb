SUMMARY = "eos Phase-1 boot beacon: prove the rootfs boots to systemd via a BCB->fastboot signal"
DESCRIPTION = "\
A systemd oneshot at multi-user.target that, when the kernel cmdline contains \
'eos-beacon', writes the misc BCB and reboots to the bootloader. The device \
landing in fastboot proves the eos rootfs booted to systemd multi-user -- an \
observable signal that needs neither WLAN/ssh nor USB/adb (both of which have \
open unknowns on this port). No-op without the cmdline flag, so the same image \
boots normally for Phase 2 (WiFi/ssh)."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
COMPATIBLE_MACHINE = "eos"

inherit systemd

SRC_URI = "file://eos-boot-beacon.sh file://eos-boot-beacon.service"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/eos-boot-beacon.sh ${D}${bindir}/eos-boot-beacon.sh
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/eos-boot-beacon.service ${D}${systemd_system_unitdir}/eos-boot-beacon.service
}

FILES:${PN} = "${bindir}/eos-boot-beacon.sh ${systemd_system_unitdir}/eos-boot-beacon.service"
SYSTEMD_SERVICE:${PN} = "eos-boot-beacon.service"
RDEPENDS:${PN} = "coreutils"
