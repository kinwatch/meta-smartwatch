# Binder-based ofono plugin: bridges ofono to Android's IRadio HAL (HIDL/AIDL)
# over binder. The piece that makes LTE work on aurora; ships binder.conf with
# InterfaceType=aidl (this watch's core radio HAL is AIDL).
# NOTE: generic — destined for meta-asteroid/recipes-connectivity upstream.

DESCRIPTION = "Binder-based ofono plugin (ofono <-> Android IRadio HAL, HIDL/AIDL)"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=37fe900f9ece53e2621d89780f2031be"

SRC_URI = "git://github.com/mer-hybris/ofono-binder-plugin.git;branch=master;protocol=https \
           file://binder.conf"
SRCREV = "2c441469c4e3e6aef5416f557998ca343ceabd47"
PV = "1.1.27+git${SRCPV}"
S = "${WORKDIR}/git"

DEPENDS = "ofono libgbinder libgbinder-radio libglibutil libdbusaccess libmce-glib glib-2.0"

inherit pkgconfig

# Upstream Makefile hard-assigns CC; override via command-line make vars.
EXTRA_OEMAKE = "KEEP_SYMBOLS=1 CC='${CC}' LD='${CC}'"
PARALLEL_MAKE = ""

# The top-level `install` drops the plugin .so into
# `pkg-config ofono --variable=plugindir`. libofonobinderpluginext (which the
# plugin links against at runtime) has its own install target under lib/ - the
# same one upstream's rpm/deb packaging uses - so call that instead of
# hand-copying a hardcoded version (the .so version bumps with upstream).
do_install() {
    oe_runmake install DESTDIR=${D}
    oe_runmake -C lib install LIBDIR=${libdir} DESTDIR=${D}
    install -d ${D}${sysconfdir}/ofono
    install -m 0644 ${UNPACKDIR}/binder.conf ${D}${sysconfdir}/ofono/binder.conf
}

FILES:${PN} += "${libdir}/ofono/plugins/*.so ${libdir}/libofonobinderpluginext.so.* ${sysconfdir}/ofono"
