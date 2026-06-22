# TEMPORARY local carry of the battery fix, pending upstream merge.
#
# Routes asteroid-launcher's notification + un-mute sounds through ngf
# instead of a QtMultimedia SoundEffect, which held a PulseAudio stream
# open for its whole lifetime and kept the audio rail from suspending (a
# standby battery drain). See the matching meta-asteroid ngfd
# "notification" event.
#
# Once these land upstream:
#   - asteroid-launcher: "Play UI sounds through ngf instead of
#     QtMultimedia SoundEffect"
#   - meta-asteroid: ngfd "notification" event
# delete this bbappend and the patch under files/.

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://0001-launcher-soundeffect-to-ngf.patch"
