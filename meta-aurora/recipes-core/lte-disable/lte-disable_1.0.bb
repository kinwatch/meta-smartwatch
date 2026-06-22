DESCRIPTION = "Power down the LTE modem (mss remoteproc) by default. EOS has no \
telephony stack to manage the Pixel Watch 2 modem yet; left running it floods \
the glink channel and pins the CPU, draining the battery (~47%/hour). Stop the \
mss remoteproc at boot until LTE is properly supported."
PR = "r0"
SRC_URI = "file://lte-disable.service \
           file://lte-disable"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0-only;md5=c79ff39f19dfec6d293b95dea7b07891"
S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"
PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "aurora"

do_install() {
    install -m 0755 -d ${D}${bindir}
    install -m 0755 lte-disable ${D}${bindir}

    install -d ${D}/etc/systemd/system/basic.target.wants/
    cp lte-disable.service ${D}/etc/systemd/system/
    ln -s ../lte-disable.service ${D}/etc/systemd/system/basic.target.wants/lte-disable.service
}
