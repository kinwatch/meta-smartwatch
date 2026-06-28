# TEMPORARY local carry of the battery fix, pending upstream merge.
#
# Plays asteroid-launcher's notification + un-mute sounds on a short-lived
# SoundEffect that is destroyed when playback ends, instead of one that lives
# for the whole launcher session. A session-lifetime SoundEffect holds its
# PulseAudio stream open forever, so the audio sink never suspends and the
# audio rail stays awake (a standby battery drain).
#
# Once this lands upstream:
#   - asteroid-launcher: "Release the notification sound's audio stream when idle"
#     (https://github.com/AsteroidOS/asteroid-launcher/pull/271)
# delete this bbappend and the patch under files/.

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://0001-launcher-oneshot-sound.patch"
