# aurora is Halium-13 / Android-13 (Wear OS 4). meta-asteroid's libgbinder ships
# gbinder.conf with ApiLevel=28 (Halium 9), under which libgbinder talks the old
# servicemanager protocol to /dev/binder and cannot reach the framework/AIDL
# services (e.g. the IRadio HAL ofono-binder-plugin needs). Override with
# ApiLevel=33 (sets /dev/binder to aidl3 protocol+servicemanager; hwbinder stays
# HIDL, so bluebinder and the HIDL HALs are unaffected).
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
