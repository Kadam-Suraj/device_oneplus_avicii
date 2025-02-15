#
# Copyright (C) 2018 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

# Inherit from those products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)

# Inherit from avicii device
$(call inherit-product, device/oneplus/avicii/device.mk)

# Inherit some common Lineage stuff.
$(call inherit-product, vendor/lineage/config/common_full_phone.mk)

# Additional stuff for this product.
TARGET_BOOT_ANIMATION_RES := 1080
TARGET_GAPPS_ARCH := arm64

PRODUCT_NAME := everest_avicii
PRODUCT_DEVICE := avicii
PRODUCT_MANUFACTURER := OnePlus
PRODUCT_BRAND := OnePlus
PRODUCT_MODEL := AC2003

PRODUCT_SYSTEM_NAME := Nord
PRODUCT_SYSTEM_DEVICE := Nord

PRODUCT_GMS_CLIENTID_BASE := android-oneplus

PRODUCT_BUILD_PROP_OVERRIDES += \
    PRIVATE_BUILD_DESC="Nord-user 12 RKQ1.211119.001 Q.202212051830:user release-keys" \
    TARGET_DEVICE=$(PRODUCT_SYSTEM_DEVICE) \
    TARGET_PRODUCT=$(PRODUCT_SYSTEM_NAME)

BUILD_FINGERPRINT := OnePlus/Nord/Nord:12/RKQ1.211119.001/Q.202212051830:user/release-keys

# Maintainer name for Everest
EVEREST_MAINTAINER := "ReVoLT"

# Boot animation
scr_resolution := 1080
TARGET_SCREEN_HEIGHT := 2400
TARGET_SCREEN_WIDTH := 1080

# Adding Blur support
TARGET_SUPPORTS_BLUR := true

# For UDFPS devices
TARGET_HAS_UDFPS := true
EXTRA_UDFPS_ANIMATIONS := true

# Build GAPPS\Vanilla
WITH_GAPPS := true

# Quick switch (add more than one Launcher in build)
TARGET_PREBUILT_LAWNCHAIR_LAUNCHER := true
TARGET_DEFAULT_PIXEL_LAUNCHER := true

# Everest Configs
TARGET_PREBUILT_BCR := true
