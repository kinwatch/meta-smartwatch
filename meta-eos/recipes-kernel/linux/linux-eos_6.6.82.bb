SUMMARY = "Pixel Watch 2 (eos) prebuilt GKI 6.6 kernel Image"
DESCRIPTION = "\
Our from-source GKI android15-6.6 kernel (device commit c2956, stock config, \
SYSVIPC=n, forged vermagic ab13752366), built out-of-tree on the Scaleway Kleaf \
builder (apps/watch/build/kernel-builder) and shipped here as a prebuilt Image. \
There is no published 6.6 device-kernel source to build in-tree, and the prebuilt \
vendor modules are KMI-pinned to this exact vermagic -- so we package the Image \
verbatim rather than rebuild. Regenerate: build/kernel-builder + copy Image here."
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

require linux-eos-version.inc
PV = "${EOS_KERNEL_VERSION}+git0"
COMPATIBLE_MACHINE = "eos"
PROVIDES = "virtual/kernel"

SRC_URI = "file://Image"
S = "${UNPACKDIR}"
KERNEL_IMAGETYPE = "Image"
KERNEL_VERSION = "${EOS_KERNEL_UTS}"

# kernel.bbclass makes kernel packages MACHINE_ARCH so an aarch64 kernel is
# installable in the (armv7) machine rootfs; mirror that for our prebuilt recipe.
# The Image is an aarch64 blob in an eos-arch package -> skip the arch QA.
PACKAGE_ARCH = "${MACHINE_ARCH}"
INSANE_SKIP:kernel-image += "arch"
INSANE_SKIP:kernel += "arch"

inherit deploy

# Prebuilt: no configure/compile. Stage the Image + an (empty) modules tree that
# eos-vendor-modules populates with 04's prebuilt .ko's.
do_install() {
    install -d ${D}/boot
    install -m 0644 ${UNPACKDIR}/Image ${D}/boot/Image
    # usrmerge: modules live under /usr/lib (${nonarch_base_libdir}), not /lib.
    install -d ${D}${nonarch_base_libdir}/modules/${EOS_KERNEL_UTS}
}

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 ${UNPACKDIR}/Image ${DEPLOYDIR}/Image
}
addtask deploy after do_install before do_build

# aurora/hybris consumers read the Image out of the kernel workdir (see
# eos-boot-images); expose the standard build path too.
do_install:append() {
    install -d ${B}/arch/arm64/boot
    install -m 0644 ${UNPACKDIR}/Image ${B}/arch/arm64/boot/Image
}

PACKAGES = "kernel kernel-image kernel-modules"
FILES:kernel-image = "/boot/Image"
FILES:kernel-modules = "${nonarch_base_libdir}/modules"
ALLOW_EMPTY:kernel-image = "1"
ALLOW_EMPTY:kernel-modules = "1"
# Built-in-module RPROVIDES live in the allarch recipe eos-kernel-module-stubs
# (the aarch64 kernel package can't be installed into the armv7 rootfs).
