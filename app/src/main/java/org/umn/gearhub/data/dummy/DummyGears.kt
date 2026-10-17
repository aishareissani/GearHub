package org.umn.gearhub.data.dummy

import org.umn.gearhub.R
import org.umn.gearhub.data.model.Gear
import org.umn.gearhub.data.model.GearCategory

object DummyGears {
    val list = listOf(
        Gear(
            id = 1,
            name = "FUJIFILM XF 50mm f/2 R WR",
            category = GearCategory.LENSA,
            pricePerDay = 50_000,
            rating = 4.8,
            reviewCount = 12,
            distanceKm = 13.1,
            ownerName = "Amanda Aliyah",
            location = "BSD City",
            description = listOf(
                "X-Mount Lens / APS-C Format",
                "76mm (35mm Equivalent)",
                "Aperture Range: f/2 to f/16",
                "One Aspherical ED Element",
                "Super EBC Coating",
                "Weather-Sealed Construction",
                "Rounded 9-Blade Diaphragm"
            ),
            specs = listOf(
                "Focal Length: 50mm (76mm Equivalent)",
                "Maximum Aperture: f/2",
                "Minimum Aperture: f/16",
                "Lens Mount: FUJIFILM X",
                "Lens Format: APS-C",
                "Focus Type: Autofocus",
                "Minimum Focus Distance: 39 cm",
                "Weight: 200 g"
            ),
            included = listOf(
                "Unit",
                "UV Filter",
                "Hood (By Request)",
                "Bag"
            ),
            images = listOf(
                R.drawable.gear_fujifilm_xf_1,
                R.drawable.gear_fujifilm_xf_2,
                R.drawable.gear_fujifilm_xf_3,
            )
        ),
        Gear(
            id = 2,
            name = "GoPro HERO12 Black",
            category = GearCategory.KAMERA,
            pricePerDay = 170_000,
            rating = 4.9,
            reviewCount = 5,
            distanceKm = 1.2,
            ownerName = "Vaurolla Az Zahra",
            location = "Gading Serpong",
            description = listOf(
                "27MP with Improved Performance",
                "5.3K60 / 4K120 / 2.7K240 Video Recording",
                "27MP Photos",
                "HyperSmooth 6.0 Image Stabilization",
                "Front and Rear LCD Screens",
                "Bluetooth Audio Connectivity and Control",
                "Waterproof up to 33 ft without Housing",
                "8x Slow-Motion Video"
            ),
            specs = listOf(
                "Sensor Resolution: 27 MP",
                "Video Recording: 5.3K at 60 fps",
                "Slow Motion: Up to 240 fps",
                "Field of View: 156°",
                "Maximum Aperture: f/2.5",
                "Image Stabilization: Digital",
                "Waterproof: Up to 10.1 m",
                "Battery Capacity: 1720 mAh"
            ),
            included = listOf(
                "GoPro Unit",
                "Protective Housing Case",
                "Tripod Mount",
                "SanDisk Extreme 64GB MicroSD Card with Adapter and Case",
                "3 Batteries with Case",
                "Charger with USB-C Cable and Adapter",
                "Carrying Bag"
            ),
            images = listOf(
                R.drawable.gear_gopro_hero12_1,
                R.drawable.gear_gopro_hero12_2,
                R.drawable.gear_gopro_hero12_3,
                R.drawable.gear_gopro_hero12_4
            )
        ),
        Gear(
            id = 3,
            name = "Saramonic Blink 500 T4 Wireless Microphone",
            category = GearCategory.AUDIO,
            pricePerDay = 90_000,
            rating = 4.9,
            reviewCount = 20,
            distanceKm = 3.4,
            ownerName = "Kenny Valent",
            location = "Kelapa Dua",
            description = listOf(
                "For Mobile Journalism and Content Creation",
                "Supports 4-Person Shoots with Various Devices",
                "4 Transmitters with Built-In Microphones",
                "Four Independent Channels",
                "Transmission Distance up to 328 ft",
                "Four 3.5mm Lavalier Microphones",
                "7-Level Gain Control and Noise Reduction",
                "USB-C Charging Ports"
            ),
            specs = listOf(
                "Transmission Range: Up to 100 m",
                "Sampling Rate / Depth: 48 kHz / 24-bit",
                "Frequency Response: 20 Hz–20 kHz",
                "Polar Pattern: Omnidirectional",
                "Signal-to-Noise Ratio: >80 dB",
                "Noise Reduction: Button-Activated",
                "Transmitter Battery Life: Up to 12 hours",
                "Receiver Battery Life: Up to 8 hours"
            ),
            included = listOf(
                "Blink500T4 TX Transmitters (4 pcs)",
                "Blink500T4 RX Receiver (1 pc)",
                "SR-M1 Omnidirectional Lavalier Microphones (4 pcs)",
                "3.5mm TRRRS to 3.5mm TRS Output Cable for Camera",
                "3.5mm TRRRS to 3.5mm TRRS Output Cable for Smartphone",
                "3.5mm TRRRS Male to Four 3.5mm TRS Male Output Cable",
                "USB-A to Triple USB-C Charging Cables (2 pcs)",
                "Adapter Chargers (2 pcs)",
                "Carrying Case"
            ),
            images = listOf(
                R.drawable.gear_saramonic_blink500_1,
                R.drawable.gear_saramonic_blink500_2,
                R.drawable.gear_saramonic_blink500_3
            )
        ),
        Gear(
            id = 4,
            name = "Godox SZ150R RGB LED Video Light",
            category = GearCategory.LIGHTING,
            pricePerDay = 120_000,
            rating = 4.9,
            reviewCount = 20,
            distanceKm = 5.8,
            ownerName = "Aisha Reissani",
            location = "Gading Serpong",
            description = listOf(
                "Color Temperature: 2800–6500K",
                "CRI 97 and TLCI 96 Color Accuracy",
                "RGB Hue, Saturation, and Intensity Control",
                "Zoomable Beam Spread from 20° to 65°",
                "13 Special Effects",
                "32 Channels and 16 Groups",
                "Silent Fan Cooling",
                "Includes Carrying Case"
            ),
            specs = listOf(
                "Photometric Output: 26,000 lux at 1 m",
                "Color Temperature: 2800–6500 K",
                "Color Modes: Full RGB",
                "Color Accuracy: CRI 97 / TLCI 96",
                "Dimming: 0–100%",
                "Beam Angle: 20–65°",
                "Wireless Control: Bluetooth, DMX, and Wi-Fi up to 50 m",
                "Maximum Power Consumption: 150 W"
            ),
            included = listOf(
                "Godox SZ150R RGB LED Video Light",
                "Reflector",
                "10 m Power Cable",
                "Light Stand",
                "Carrying Bag"
            ),
            images = listOf(
                R.drawable.gear_godox_sz150r_1,
                R.drawable.gear_godox_sz150r_2,
                R.drawable.gear_godox_sz150r_3,
                R.drawable.gear_godox_sz150r_4
            )
        ),
        Gear(
            id = 5,
            name = "RODE VideoMic GO II Helix Shotgun Microphone",
            category = GearCategory.AUDIO,
            pricePerDay = 25_000,
            rating = 4.9,
            reviewCount = 20,
            distanceKm = 2.1,
            ownerName = "Wonyoung",
            location = "Medang",
            description = listOf(
                "Designed for Vlogging, Run-and-Gun, Voiceover, and Podcasting",
                "Directional Supercardioid Pickup Pattern",
                "HELIX Isolation Shock Mount",
                "3.5mm TRS Output for Camera or Headphones",
                "USB-C Output for Mac, Windows, iOS, and Android",
                "Ultralightweight Design at 2.1 oz",
                "No Battery Required",
                "Windscreen Reduces Wind Noise",
                "Compatible with RØDE Apps"
            ),
            specs = listOf(
                "Microphone Type: Shotgun",
                "Polar Pattern: Supercardioid",
                "Frequency Response: 20 Hz–20 kHz",
                "Maximum SPL: 110 dB",
                "Dynamic Range: 95 dB",
                "Digital Audio: 24-bit / 48 kHz",
                "Connectivity: USB-C and 3.5 mm TRS",
                "Weight: 33 g (microphone only)"
            ),
            included = listOf(
                "RODE VideoMic GO II Microphone",
                "HELIX Isolation Mount",
                "Windscreen",
                "TRS to TRS Audio Cable",
                "Pouch"
            ),
            images = listOf(
                R.drawable.gear_rode_videomic_go2_1,
                R.drawable.gear_rode_videomic_go2_2,
                R.drawable.gear_rode_videomic_go2_3
            )
        ),
        Gear(
            id = 6,
            name = "Insta360 X5 360° 8K Camera",
            category = GearCategory.KAMERA,
            pricePerDay = 210_000,
            rating = 4.7,
            reviewCount = 13,
            distanceKm = 8.6,
            ownerName = "Taylor Swift",
            location = "Pagedangan",
            description = listOf(
                "8K30 and 5.7K30 360° PureVideo Recording",
                "360° Reframing in Post-Production",
                "Extended Battery Runtime",
                "Gesture Control",
                "Dual-Lens and Single-Lens Modes",
                "Bullet Time and Time-Lapse Modes",
                "InstaFrame Mode",
                "Active HDR and Loop Recording",
                "Me Mode",
                "Waterproof up to 49 ft",
                "72MP Photos",
                "App with Editing Tools and Effects"
            ),
            specs = listOf(
                "Video Resolution: 8K 360° (7680 × 4320)",
                "Image Resolution: 72 MP",
                "Image Sensor: 1/1.28-inch CMOS",
                "Maximum Aperture: f/2",
                "Field of View: 170° per lens",
                "Video Recording: 4K UHD up to 60 fps",
                "Waterproof: Up to 10 m",
                "Battery Life: Up to 3 hours per charge"
            ),
            included = listOf(
                "Insta360 X5 360° 8K Camera",
                "Premium Lens Guards (2 pcs)",
                "64GB Memory Card",
                "Battery",
                "Adapter Charger and USB-C Cable",
                "Protective Case",
                "Pouch"
            ),
            images = listOf(
                R.drawable.gear_insta360_x5_1,
                R.drawable.gear_insta360_x5_2,
                R.drawable.gear_insta360_x5_3,
                R.drawable.gear_insta360_x5_4
            )
        ),
        Gear(
            id = 7,
            name = "DJI RS 5 Gimbal Stabilizer - Combo",
            category = GearCategory.KAMERA,
            pricePerDay = 180_000,
            rating = 4.6,
            reviewCount = 27,
            distanceKm = 4.5,
            ownerName = "NIKI",
            location = "Karawaci",
            description = listOf(
                "Lightweight 3.2 lb Body with 6.6 lb Payload Capacity",
                "Z-Axis Vertical Indicator",
                "Active Tracking Module",
                "Briefcase Handle",
                "Full Charge in One Hour with 65W PD Charger",
                "2nd-Generation Automated Axis Locks",
                "Separate Electronic Control Handle",
                "OLED Touchscreen with Auto-Lock",
                "2nd-Generation Native Vertical Shooting",
                "Joystick or Bluetooth Mode Switching",
                "Teflon-Coated Axes for Enhanced Balancing"
            ),
            specs = listOf(
                "Load Capacity: 3 kg",
                "Number of Axes: 3-Axis Stabilization",
                "Rotation Range: 360° Pan",
                "Follow Speed: 360°/s",
                "Battery Runtime: Up to 14 hours",
                "Battery Charging Time: 1 hour",
                "Wireless Connectivity: 2.4 GHz / Bluetooth 5.1",
                "Display: Fixed OLED Touchscreen"
            ),
            included = listOf(
                "DJI RS 5 Gimbal Stabilizer",
                "DJI RS Enhanced Intelligent Tracking Module",
                "DJI Electronic Briefcase Handle",
                "DJI RS BG33 Battery Grip",
                "Quick-Open Tripod",
                "Upper Quick Release Plate",
                "Lower Quick Release Plate",
                "Quick Release Plate Pair",
                "USB-C Multicamera Cable",
                "USB-C L-Shaped Cable",
                "Adapter Charger and USB-C to USB-C Cable",
                "Carrying Bag"
            ),
            images = listOf(
                R.drawable.gear_dji_rs5_1,
                R.drawable.gear_dji_rs5_2,
                R.drawable.gear_dji_rs5_3
            )
        ),
        Gear(
            id = 8,
            name = "Godox V1Pro S Flash for Sony",
            category = GearCategory.LIGHTING,
            pricePerDay = 50_000,
            rating = 3.9,
            reviewCount = 6,
            distanceKm = 11.2,
            ownerName = "Laufey",
            location = "Tangerang Kota",
            description = listOf(
                "Compatible with Sony ADI / P-TTL",
                "76Ws Flash Output",
                "Auto Zoom Control with 28–105mm Zoom Range",
                "2.4 GHz X Wireless Radio System",
                "Built-In LED Modeling Lamp",
                "Tilts from -7° to 120° and Rotates 330°",
                "Round Flash Head with Optional Magnetic Modifiers",
                "1.5-Second Recycle Time",
                "Supports High-Speed Sync"
            ),
            specs = listOf(
                "Coverage: Full-Frame, 28–105 mm",
                "Bounce Head: -7° to +120°",
                "Swivel Head: 330°",
                "Recycle Time: Approx. 1.5 seconds",
                "Power Range: 1/1 to 1/256",
                "Flash Compensation: -3 to +3 EV",
                "Wireless Operation: Radio TTL / Manual, up to 100 m",
                "Flash Duration: 1/300 to 1/20,000 seconds"
            ),
            included = listOf(
                "Godox V1Pro S Flash",
                "Battery",
                "Power Adapter and Cable",
                "USB-C Charging Cable",
                "Flash Stand",
                "Godox Sub-Flash for V1Pro",
                "Pouch"
            ),
            images = listOf(
                R.drawable.gear_godox_v1pro_1,
                R.drawable.gear_godox_v1pro_2,
                R.drawable.gear_godox_v1pro_3
            )
        ),
        Gear(
            id = 9,
            name = "Fujifilm Instax Mini 99 Instant Film Camera",
            category = GearCategory.KAMERA,
            pricePerDay = 80_000,
            rating = 4.7,
            reviewCount = 35,
            distanceKm = 6.9,
            ownerName = "Adela",
            location = "Pondok Aren",
            description = listOf(
                "Prints 2 × 3-inch Instant Photos",
                "Five Shooting Modes with Auto Exposure",
                "Landscape and Macro Modes",
                "Built-In Flash Control",
                "Five Brightness Levels",
                "Six Color Effect Settings",
                "Manual Vignette Switch",
                "Built-In Self-Timer",
                "Two Shutter Buttons for Portrait and Landscape"
            ),
            specs = listOf(
                "Film Format: INSTAX Mini",
                "Focal Length: 60 mm",
                "Aperture: f/12.7",
                "Focus Type: Zone Focus",
                "Minimum Focus Distance: 0.3 m",
                "Shutter Speed: 1/400 to 18 seconds",
                "Built-In Flash: Yes, effective range 0.3–2.7 m",
                "Self-Timer: 10 seconds"
            ),
            included = listOf(
                "Fujifilm Instax Mini 99 Camera",
                "Shoulder Strap",
                "USB-C Cable",
                "Battery Charger BC-70C",
                "Battery NP-70S",
                "Base Grip with Tripod Mount",
                "Carrying Bag"
            ),
            images = listOf(
                R.drawable.gear_instax_mini99_1,
                R.drawable.gear_instax_mini99_2,
                R.drawable.gear_instax_mini99_3,
                R.drawable.gear_instax_mini99_4
            )
        ),
        Gear(
            id = 10,
            name = "Nikon Z5",
            category = GearCategory.KAMERA,
            pricePerDay = 175_000,
            rating = 4.8,
            reviewCount = 19,
            distanceKm = 2.7,
            ownerName = "Olivia Rodrigo",
            location = "Alam Sutera",
            description = listOf(
                "24.3MP FX-Format CMOS Sensor",
                "EXPEED 6 Image Processor",
                "UHD 4K and Full HD Video Recording",
                "3.6M-Dot OLED Electronic Viewfinder",
                "3.2-inch 1.04M-Dot Tilting Touchscreen LCD",
                "5-Axis Sensor-Shift Vibration Reduction",
                "ISO 100–51200",
                "Continuous Shooting up to 4.5 fps",
                "Built-In Wi-Fi and Bluetooth",
                "Dual SD UHS-II Card Slots"
            ),
            specs = listOf(
                "Effective Sensor Resolution: 24.3 MP",
                "Image Sensor: Full-Frame CMOS, 35.9 × 23.9 mm",
                "Image Stabilization: 5-Axis Sensor-Shift",
                "ISO Sensitivity: 100–51,200 (Expandable to 102,400)",
                "Shutter Speed: 1/8000 to 30 seconds",
                "Video Recording: 4K UHD up to 29.97 fps",
                "Autofocus Points: 273 Phase-Detection Points",
                "Continuous Shooting: Up to 4.5 fps"
            ),
            included = listOf(
                "Nikon Z5 Camera",
                "Memory Card",
                "Memory Card Case",
                "Battery",
                "Charger",
                "Carrying Bag"
            ),
            images = listOf(
                R.drawable.gear_nikon_z5_1,
                R.drawable.gear_nikon_z5_2,
                R.drawable.gear_nikon_z5_3
            )
        )
    )

    fun getById(id: Int): Gear? = list.find { it.id == id }
}