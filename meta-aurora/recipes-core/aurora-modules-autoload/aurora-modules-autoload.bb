SUMMARY = "Auto-load post-rootfs aurora kernel modules + select deep-sleep cpuidle governor"
DESCRIPTION = "Drops a systemd-modules-load.d config that loads the WLAN, \
ALSA/ASoC machine + codec, BT slim, and power-management (RPM VDD_MIN) modules \
shipped on the rootfs by linux-aurora-modules. These cannot live in \
modules.load.aurora because their .ko files are only reachable AFTER pivot_root \
to the rootfs (the VKB ramdisk holds only the boot-critical modules). Also ships \
a oneshot that selects the qcom_lpm cpuidle governor, without which the SoC never \
reaches VDD_MIN during suspend (~37-52 mA standby floor)."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
COMPATIBLE_MACHINE = "aurora"

inherit systemd

SRC_URI = "file://aurora-post-rootfs.conf \
           file://aurora-cpuidle-lpm.service"

do_install() {
    install -d -m 0755 ${D}${sysconfdir}/modules-load.d
    install -m 0644 ${UNPACKDIR}/aurora-post-rootfs.conf ${D}${sysconfdir}/modules-load.d/aurora-post-rootfs.conf

    install -d -m 0755 ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/aurora-cpuidle-lpm.service ${D}${systemd_system_unitdir}/aurora-cpuidle-lpm.service
}

FILES:${PN} = "${sysconfdir}/modules-load.d/aurora-post-rootfs.conf \
               ${systemd_system_unitdir}/aurora-cpuidle-lpm.service"

SYSTEMD_SERVICE:${PN} = "aurora-cpuidle-lpm.service"

RDEPENDS:${PN} = "linux-aurora-modules"
