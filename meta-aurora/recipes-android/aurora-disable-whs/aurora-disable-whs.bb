SUMMARY = "Disable the Qualcomm Wearable Health Services HAL (standby battery fix)"
DESCRIPTION = "vendor.qti.hardware.healthservices@1.0-service runs continuous \
optical HR/PPG on wear and holds a BLOCK_SUSPEND wakelock, draining ~48 mA and \
blocking deep sleep (watch died overnight). Nothing in AsteroidOS uses it (HR is \
on-demand via asteroid-hrm). /vendor is read-only, so a boot oneshot stops the \
service from the host once the container's HALs are up."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
COMPATIBLE_MACHINE = "aurora"

inherit systemd

SRC_URI = "file://aurora-disable-whs.sh \
           file://aurora-disable-whs.service"

do_install() {
    install -d -m 0755 ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/aurora-disable-whs.sh ${D}${bindir}/aurora-disable-whs.sh

    install -d -m 0755 ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/aurora-disable-whs.service ${D}${systemd_system_unitdir}/aurora-disable-whs.service
}

FILES:${PN} = "${bindir}/aurora-disable-whs.sh \
               ${systemd_system_unitdir}/aurora-disable-whs.service"

SYSTEMD_SERVICE:${PN} = "aurora-disable-whs.service"

RDEPENDS:${PN} = "lxc"
