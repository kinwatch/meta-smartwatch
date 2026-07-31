# Single source of truth for the eos kernel release string. Must equal the
# device UTS_RELEASE so the prebuilt vendor modules load (forged via
# BUILD_NUMBER=13752366 at build time in build/kernel-builder/).
EOS_KERNEL_VERSION = "6.6.82"
EOS_KERNEL_UTS = "6.6.82-android15-8-gc2956ea7d496-ab13752366-4k"
