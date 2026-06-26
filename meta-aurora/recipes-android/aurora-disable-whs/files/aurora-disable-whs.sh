#!/bin/sh
# Disable the Qualcomm Wearable Health Services (WHS) HAL in the Android container.
#
# vendor.qti.hardware.healthservices@1.0-service runs continuous optical HR/PPG
# (green LED) the moment the watch is worn (skin-contact / off-body detection),
# and its init .rc grants `group wakelock` + `capabilities BLOCK_SUSPEND`. Nothing
# in AsteroidOS consumes it (HR is on-demand via the asteroid-hrm app, a separate
# request), so on-wrist it just burned ~48 mA continuously and blocked deep sleep
# -> watch dead overnight. Found 2026-06-26: stopping it makes the green LED stay
# off on wear, with no loss of AsteroidOS function.
#
# /vendor is read-only (can't add `disabled` to the .rc), so stop it from the host
# after the container's HALs start. Android `ctl.stop` keeps it stopped until the
# next boot; this oneshot re-applies it every boot. The brief run during boot is
# harmless (you're not wearing it then).

LXC="lxc-attach -n android --"
SVC=healthservices-hal-1-0
i=0
while [ "$i" -lt 90 ]; do
    st=$($LXC /system/bin/getprop init.svc.$SVC 2>/dev/null)
    if [ "$st" = "running" ]; then
        $LXC /system/bin/setprop ctl.stop $SVC 2>/dev/null
        sleep 1
        st2=$($LXC /system/bin/getprop init.svc.$SVC 2>/dev/null)
        [ "$st2" = "stopped" ] && { echo "aurora-disable-whs: $SVC stopped"; exit 0; }
    fi
    i=$((i+1)); sleep 1
done
echo "aurora-disable-whs: gave up after ${i}s (last state='$st')" >&2
exit 0
