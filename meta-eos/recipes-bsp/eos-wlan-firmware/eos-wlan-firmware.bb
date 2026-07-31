SUMMARY = "Pixel Watch 2 (eos) WLAN firmware (from 04 factory vendor)"
DESCRIPTION = "\
qcacld/CNSS WLAN firmware extracted from unit 04's factory vendor partition: \
board data (bdwlan.bin), the WLAN co-processor image (wlanmdsp.mbn) and the \
qca_cld ini. Shipped to /lib/firmware so the kernel firmware loader finds it \
without mounting /vendor -- enough for the WiFi milestone. (The full dm-linear \
/vendor mount, eos-vendor-mount, is deferred with the HAL work.)"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"
COMPATIBLE_MACHINE = "eos"

SRC_URI = "file://eos-wlan-firmware.tar.gz"
S = "${UNPACKDIR}"

do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware/wlan/qca_cld
    install -m 0644 ${UNPACKDIR}/bdwlan.bin   ${D}${nonarch_base_libdir}/firmware/
    install -m 0644 ${UNPACKDIR}/wlanmdsp.mbn ${D}${nonarch_base_libdir}/firmware/
    install -m 0644 ${UNPACKDIR}/wlan/qca_cld/WCNSS_qcom_cfg.ini ${D}${nonarch_base_libdir}/firmware/wlan/qca_cld/
}

FILES:${PN} = "${nonarch_base_libdir}/firmware"
INSANE_SKIP:${PN} += "arch"
