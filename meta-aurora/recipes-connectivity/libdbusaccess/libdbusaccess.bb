# NOTE: generic AsteroidOS telephony dep — destined for meta-asteroid/recipes-connectivity
# upstream; in meta-aurora for the aurora LTE trial build for now.
# D-Bus access-control helper (Sailfish/Mer). Needed by libdbuslog and the
# Sailfish ofono fork. Modeled on meta-asteroid's libgbinder.bb.

DESCRIPTION = "D-Bus access-control helper library (Sailfish/Mer)."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=80264d9978170503c3f7b5e5eac1ccf1"

SRC_URI = "git://github.com/sailfishos/libdbusaccess.git;branch=master;protocol=https"
SRCREV = "1.0.20"
PV = "+git${SRCPV}"
S = "${WORKDIR}/git"

# bison-native: libdbusaccess builds a bison parser for its access-spec syntax.
DEPENDS = "glib-2.0 libglibutil bison-native"

inherit pkgconfig

# Pass CC/LD as make command-line vars (override any hard `CC=` in the Makefile;
# harmless for the `CC ?=` variants) so OE's cross compiler is used, not native gcc.
EXTRA_OEMAKE = "KEEP_SYMBOLS=1 CC='${CC}' LD='${CC}'"
PARALLEL_MAKE = ""

do_install() {
    make install DESTDIR=${D}
    make install-dev DESTDIR=${D}
}
