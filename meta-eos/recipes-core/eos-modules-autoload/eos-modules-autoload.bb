SUMMARY = "eos: auto-load WLAN vendor modules after rootfs mount"
DESCRIPTION = "\
Drops a systemd-modules-load.d config that loads qcacld (wlan) + the CNSS glue \
after pivot_root, when the prebuilt .ko's shipped by eos-vendor-modules become \
reachable at /lib/modules/<uts>/vendor. modprobe resolves deps from modules.dep. \
Audio/BT/display autoloads are deferred with the HAL work."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
COMPATIBLE_MACHINE = "eos"

SRC_URI = "file://eos-post-rootfs.conf"

do_install() {
    install -d -m 0755 ${D}${sysconfdir}/modules-load.d
    install -m 0644 ${UNPACKDIR}/eos-post-rootfs.conf ${D}${sysconfdir}/modules-load.d/eos-post-rootfs.conf
}

FILES:${PN} = "${sysconfdir}/modules-load.d/eos-post-rootfs.conf"
