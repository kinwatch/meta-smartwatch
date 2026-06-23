DESCRIPTION = "Bring up and maintain the LTE data connection from ofono's \
ConnectionContext settings. connman does not manage the cellular service on this \
port, and the rmnet data interface is rawip (IPv4 default route must be 'onlink'); \
the data call also lands on a varying rmnet_dataN mux that must be read live from \
ofono. This service applies Interface + IPv4 (addr + onlink route) + IPv6 + DNS \
and keeps the internet context active. Validated on-device: dual-stack, 0% loss \
over 5 min. Only meaningful with LTE enabled (ofono); inert if lte-disable holds \
the modem off."
PR = "r0"
SRC_URI = "file://lte-data.service \
           file://lte-data-up"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0-only;md5=c79ff39f19dfec6d293b95dea7b07891"
S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"
PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "aurora"
RDEPENDS:${PN} = "ofono iproute2"

do_install() {
    install -m 0755 -d ${D}${bindir}
    install -m 0755 lte-data-up ${D}${bindir}

    install -d ${D}/etc/systemd/system/basic.target.wants/
    cp lte-data.service ${D}/etc/systemd/system/
    ln -s ../lte-data.service ${D}/etc/systemd/system/basic.target.wants/lte-data.service
}
