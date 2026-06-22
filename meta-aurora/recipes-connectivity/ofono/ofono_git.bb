# Sailfish/Mer ofono fork — the telephony daemon that hosts ofono-binder-plugin
# (upstream oe-core ofono lacks the plugin API the binder plugin needs).
# Pinned over oe-core's ofono_2.15 via PREFERRED_VERSION_ofono = "1.29+git%".
# NOTE: generic — destined for meta-asteroid/recipes-connectivity upstream;
# in meta-aurora for the aurora LTE trial build for now.

DESCRIPTION = "open source telephony (Sailfish/Mer fork with the ofono plugin API)"
HOMEPAGE = "https://github.com/sailfishos/ofono"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

SRC_URI = "git://github.com/sailfishos/ofono.git;branch=master;protocol=https"
SRCREV = "3331ee9b89ffb732e5c6e0d0b3b8e1e2d2353927"
PV = "1.29+git${SRCPV}"
# The sailfishos/ofono repo nests the actual ofono source in an ofono/ subdir
# (repo root holds ofono/, rpm/, upstream/).
S = "${WORKDIR}/git/ofono"
# ofono's AM_CPPFLAGS has -I$(builddir)/include and -I$(srcdir)/src but NOT
# -I$(srcdir)/include, so its static public headers (e.g. include/sim-mnclength.h)
# aren't found in OE's out-of-tree build. Add the srcdir include dir explicitly.
CFLAGS:append = " -I${S}/include"

DEPENDS = "dbus dbus-glib glib-2.0 glib-2.0-native udev \
           mobile-broadband-provider-info \
           libglibutil libdbusaccess libdbuslog libwspcodec"

inherit autotools pkgconfig systemd

SYSTEMD_SERVICE:${PN} = "ofono.service"

# Sailfish configure flags (from the fork's rpm spec). Built-in modems disabled
# — the radio path comes from ofono-binder-plugin via the AIDL IRadio HAL.
EXTRA_OECONF = "--disable-test \
                --enable-sailfish-bt \
                --enable-sailfish-debuglog \
                --enable-sailfish-provision \
                --disable-sailfish-pushforwarder \
                --enable-sailfish-access \
                --disable-add-remove-context \
                --disable-rilmodem \
                --disable-isimodem \
                --disable-qmimodem \
                --disable-static"

FILES:${PN} += "${systemd_unitdir} ${libdir}/ofono"
RDEPENDS:${PN} += "dbus mobile-broadband-provider-info"
