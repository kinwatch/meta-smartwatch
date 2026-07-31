SUMMARY = "Pixel Watch 2 (eos) prebuilt vendor kernel modules"
DESCRIPTION = "\
The 250 prebuilt Qualcomm/Google vendor .ko's extracted from unit 04's factory \
vendor_dlkm (build BP3A.250905.014.W3). We reuse the prebuilt modules rather than \
rebuild from source (as the 5.15 aurora port does) because Google has not published \
the 6.6 device-module source; they are KMI-pinned to our kernel's forged vermagic \
(ab13752366) so they load by construction. Shipped to the rootfs so post-boot loads \
(WLAN etc.) resolve. Regenerate from a factory image: extract vendor_dlkm.img (ext4, \
debugfs rdump) and tar its /lib/modules."
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"
COMPATIBLE_MACHINE = "eos"

require recipes-kernel/linux/linux-eos-version.inc

SRC_URI = "file://eos-vendor-modules.tar.gz"
S = "${UNPACKDIR}"

# Prebuilt aarch64 .ko's on an armv7 userspace: skip the arch/strip/etc. QA.
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_SYSROOT_STRIP = "1"
INSANE_SKIP:${PN} += "arch already-stripped ldflags buildpaths"

do_install() {
    # usrmerge: modules under /usr/lib (${nonarch_base_libdir}), not /lib.
    install -d ${D}${nonarch_base_libdir}/modules/${EOS_KERNEL_UTS}/vendor
    cp ${UNPACKDIR}/*.ko ${D}${nonarch_base_libdir}/modules/${EOS_KERNEL_UTS}/vendor/
    # Ship the factory dep/alias/softdep too; systemd-modules-load's modprobe uses them.
    for f in modules.dep modules.alias modules.softdep; do
        [ -f ${UNPACKDIR}/$f ] && install -m 0644 ${UNPACKDIR}/$f ${D}${nonarch_base_libdir}/modules/${EOS_KERNEL_UTS}/ || true
    done
}

FILES:${PN} = "${nonarch_base_libdir}/modules"
# .ko's are the payload; nothing to strip/split.
