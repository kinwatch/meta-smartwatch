# NOTE: generic AsteroidOS telephony dep — destined for meta-asteroid/recipes-connectivity
# upstream; in meta-aurora for the aurora LTE trial build for now.
# WSP (Wireless Session Protocol) codec library (Sailfish/Mer); a build dep of
# the Sailfish ofono fork (MMS push). Upstream Makefile hard-assigns CC.

DESCRIPTION = "WSP (Wireless Session Protocol) codec library (Sailfish/Mer)."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ade4293688443d2901fcd5ea408f0f3"

SRC_URI = "git://github.com/sailfishos/libwspcodec.git;branch=master;protocol=https"
SRCREV = "2.2.6"
PV = "+git${SRCPV}"
S = "${WORKDIR}/git"

DEPENDS = "glib-2.0"

inherit pkgconfig

EXTRA_OEMAKE = "KEEP_SYMBOLS=1 CC='${CC}' LD='${CC}'"
PARALLEL_MAKE = ""

do_install() {
    make install DESTDIR=${D}
    make install-dev DESTDIR=${D}
}
