# NOTE: generic AsteroidOS telephony lib — destined for meta-asteroid/recipes-connectivity
# upstream; kept in meta-aurora for the aurora LTE trial build for now.
# Helper for communicating with Android's IRadio binder interface (HIDL + AIDL).
# Required by ofono-binder-plugin. Modeled on meta-asteroid's libgbinder.bb.

DESCRIPTION = "Helper library for communicating with Android's IRadio binder interface (HIDL + AIDL)."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=11fd710e50bd899453f380488715f133"

SRC_URI = "git://github.com/mer-hybris/libgbinder-radio.git;branch=master;protocol=https"
SRCREV = "1.6.3"
PV = "+git${SRCPV}"
S = "${WORKDIR}/git"

DEPENDS = "glib-2.0 libglibutil libgbinder"

inherit pkgconfig

# Upstream Makefile hard-assigns `CC = $(CROSS_COMPILE)gcc` (unlike libgbinder's
# `CC ?=`), so it ignores OE's cross compiler and builds with native gcc -> the
# target glib headers (32-bit, gsize=4) vs native pointers (8) trip a
# g_once_init_enter static assert. Pass CC/LD as make command-line vars, which
# override the Makefile's `=` assignment.
EXTRA_OEMAKE = "KEEP_SYMBOLS=1 CC='${CC}' LD='${CC}'"
PARALLEL_MAKE = ""

do_install() {
    make install DESTDIR=${D}
    make install-dev DESTDIR=${D}
}
