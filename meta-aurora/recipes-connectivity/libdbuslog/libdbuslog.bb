# NOTE: generic AsteroidOS telephony dep — destined for meta-asteroid/recipes-connectivity
# upstream; in meta-aurora for the aurora LTE trial build for now.
# D-Bus log server/client library (Sailfish/Mer); provides libdbuslogserver-dbus,
# a build dep of the Sailfish ofono fork (--enable-sailfish-debuglog).

DESCRIPTION = "D-Bus log server/client library (Sailfish/Mer)."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a18b5abfa17357df72ddbd5ea77e6a94"

SRC_URI = "git://github.com/sailfishos/libdbuslog.git;branch=master;protocol=https"
SRCREV = "1.0.22"
PV = "+git${SRCPV}"
S = "${WORKDIR}/git"

# glib-2.0-native: provides gdbus-codegen (build-time) for the D-Bus interface code.
DEPENDS = "glib-2.0 libglibutil libdbusaccess dbus glib-2.0-native"

inherit pkgconfig

EXTRA_OEMAKE = "KEEP_SYMBOLS=1 CC='${CC}' LD='${CC}'"
PARALLEL_MAKE = ""

# libdbuslog's server sub-Makefile links its .so without threading OE's LDFLAGS,
# so the libs miss GNU_HASH. The other Monich libs here pass it fine; skip the
# check just for this recipe (GNU_HASH is a symbol-lookup optimization, not
# correctness).
INSANE_SKIP:${PN} += "ldflags"

# Mixed install conventions across subdirs: top-level `install` ships the client
# fully (lib + .pc + headers) but only the server .so; the server's .pc + headers
# come via its own `install-dev`. The client subdir has no `install-dev` (so a
# top-level `install-dev` errors). Run both pieces explicitly so
# libdbuslogserver-dbus.pc (needed by ofono) is installed.
do_install() {
    oe_runmake install DESTDIR=${D}
    oe_runmake -C server install-dev DESTDIR=${D}
}
