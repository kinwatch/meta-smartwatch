DESCRIPTION = "Online the LTE modem at boot so connman can manage cellular data. \
connman's ofono plugin owns the data path (creates the cellular service, activates \
the context, configures IPv4/IPv6/DNS/route, arbitrates with WiFi, shows in the \
UI) once the modem is online+registered -- but it does not online the modem \
itself. This oneshot sets the modem Online (toggling to force acquisition); \
connman does the rest. Validated on-device: dual-stack, 0% loss, DNS. Only \
meaningful with LTE enabled (ofono); inert if lte-disable holds the modem off."
PR = "r1"
SRC_URI = "file://lte-data.service \
           file://lte-data-up"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0-only;md5=c79ff39f19dfec6d293b95dea7b07891"
S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"
PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "aurora"
RDEPENDS:${PN} = "ofono"

do_install() {
    install -m 0755 -d ${D}${bindir}
    install -m 0755 lte-data-up ${D}${bindir}

    install -d ${D}/etc/systemd/system/basic.target.wants/
    cp lte-data.service ${D}/etc/systemd/system/
    ln -s ../lte-data.service ${D}/etc/systemd/system/basic.target.wants/lte-data.service
}
