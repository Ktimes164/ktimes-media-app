package com.example.data.models

data class MarketplaceAddon(
    val id: String,
    val title: String,
    val price: Int,
    val formattedPrice: String,
    val description: String = ""
)

enum class MarketplaceCategory(val code: String, val marathiLabel: String, val englishLabel: String) {
    ALL("ALL", "सर्व सेवा", "All Services"),
    VIDEO("VIDEO", "व्हिडिओ जाहिराती", "Video Commercials"),
    AUDIO("AUDIO", "ऑडिओ व जिंन्गल्स", "Audio & Jingles"),
    BANNER("BANNER", "बॅनर्स व फ्लेक्स", "Banners & Flex"),
    AI_VIDEO("AI_VIDEO", "AI व्हिडिओ व व्हॉइस", "AI Video & Voices"),
    SOCIAL_MEDIA("SOCIAL_MEDIA", "सोशल मीडिया बंडल", "Social Media Packages")
}

data class MarketplaceService(
    val id: String,
    val serviceCode: String,
    val title: String,
    val marathiTitle: String,
    val category: MarketplaceCategory,
    val basePrice: String,
    val priceNumber: Int,
    val deliveryTime: String,
    val shortDescription: String,
    val deliverables: List<String>,
    val addons: List<MarketplaceAddon> = emptyList(),
    val isPopular: Boolean = false,
    val badgeText: String = "",
    val iconType: String = "video"
)

object MarketplaceData {
    val services = listOf(
        // ==================== 1. VIDEO SERVICES ====================
        MarketplaceService(
            id = "srv_vid_01",
            serviceCode = "VID-4K-COMMERCIAL",
            title = "4K Commercial TVC & Business Video",
            marathiTitle = "४K टीव्हीसी / व्यावसायिक व्हिडिओ जाहिरात",
            category = MarketplaceCategory.VIDEO,
            basePrice = "₹४,९९९",
            priceNumber = 4999,
            deliveryTime = "२४ ते ४८ तास",
            shortDescription = "तुमच्या व्यवसायासाठी, दुकानासाठी किंवा ब्रँडसाठी सिनेमॅटिक ४K दर्जाची जाहिरात. ३D मोशन टायटल्स व स्टुडिओ निवेदन.",
            deliverables = listOf(
                "4K Ultra HD व्हिडिओ (16:9 TV & YouTube + 9:16 Instagram Reels)",
                "प्रोफेशनल मराठी / हिंदी व्हॉईसओव्हर रेकॉर्डिंग",
                "३D लोगो अॅनिमेशन आणि मोशन ग्राफिक्स",
                "रॉयल्टी-फ्री सिनेमॅटिक पार्श्वसंगीत",
                "३ मोफत सुधारणा (Revisions)"
            ),
            addons = listOf(
                MarketplaceAddon("add_express_24", "अतिजलद २४ तास डिलिव्हरी (Express 24h)", 999, "+₹९९९"),
                MarketplaceAddon("add_drone_shot", "ड्रोन शॉट्स / एक्स्ट्रा सिनेमॅटिक इफेक्ट्स", 1499, "+₹१,४९९"),
                MarketplaceAddon("add_hindi_ver", "अतिरिक्त हिंदी व्हर्जन रूपांतर", 799, "+₹७९९")
            ),
            isPopular = true,
            badgeText = "🔥 मोस्ट पॉप्युलर",
            iconType = "video"
        ),
        MarketplaceService(
            id = "srv_vid_02",
            serviceCode = "VID-REELS-SHORTS",
            title = "Instagram Reels & Shorts Video Promo",
            marathiTitle = "इन्स्टाग्राम रील्स व शॉर्ट्स प्रोमो व्हिडिओ",
            category = MarketplaceCategory.VIDEO,
            basePrice = "₹१,९९९",
            priceNumber = 1999,
            deliveryTime = "२४ तास",
            shortDescription = "सोशल मीडियावर व्हायरल होणारा हाय-एंगेजमेंट 9:16 व्हर्टिकल व्हिडिओ. कॅप्शन्स, ट्रेंडिंग म्युझिक आणि आकर्षक हुक्स.",
            deliverables = listOf(
                "Full HD 1080x1920 व्हर्टिकल 9:16 फॉर्मॅट",
                "आकर्षक ऑन-स्क्रीन मराठी / इंग्रजी सबटायटल्स",
                "ट्रेंडिंग ट्रान्झिशन व साऊंड इफेक्ट्स (SFX)",
                "कॉल-टू-अ‍ॅक्शन (CTA) व व्यवसाय पत्ता"
            ),
            addons = listOf(
                MarketplaceAddon("add_pack_3", "३ रील्सचा कॉम्बो पॅक (Save 20%)", 3499, "+₹३,४९९"),
                MarketplaceAddon("add_captions_pro", "अॅनिमेटेड कलरफुल टायपोग्राफी", 499, "+₹४९९")
            ),
            isPopular = true,
            badgeText = "⚡ FAST 24H",
            iconType = "video"
        ),
        MarketplaceService(
            id = "srv_vid_03",
            serviceCode = "VID-3D-EXPLAINER",
            title = "3D Product Animation & Motion Graphics",
            marathiTitle = "३D प्रॉडक्ट अॅनिमेशन व मोशन ग्राफिक्स",
            category = MarketplaceCategory.VIDEO,
            basePrice = "₹५,९९९",
            priceNumber = 5999,
            deliveryTime = "३ ते ४ दिवस",
            shortDescription = "वस्तू, शोरूम किंवा तंत्रज्ञानाचे ३D मॉडेलिंग व ३६०° फिरणारे अॅनिमेशन.",
            deliverables = listOf(
                "3D कॅमेरा मूव्हमेंट व प्रॉडक्ट रोटेशन",
                "हाय-एंड टेक्सचर व व्हिज्युअल इफेक्ट्स",
                "व्हॉईसओव्हर सिंक आणि साऊंड डिझायनिंग"
            ),
            addons = listOf(
                MarketplaceAddon("add_source_3d", "मूळ ३D सोर्स फाईल्स व रेंडर", 1999, "+₹१,९९९")
            ),
            isPopular = false,
            badgeText = "💎 PREMIUM 3D",
            iconType = "video"
        ),
        MarketplaceService(
            id = "srv_vid_04",
            serviceCode = "VID-ELECTION-CAMPAIGN",
            title = "Political Election Campaign Video (प्रचार व्हिडिओ)",
            marathiTitle = "निवडणूक प्रचार धडाकेबाज व्हिडिओ",
            category = MarketplaceCategory.VIDEO,
            basePrice = "₹३,४९९",
            priceNumber = 3499,
            deliveryTime = "२४ तास",
            shortDescription = "उमेदवाराची विकासकामे, जनसंपर्क आणि निवडणूक चिन्ह हायलाइट करणारा खणखणीत व्हिडिओ.",
            deliverables = listOf(
                "उमेदवाराचा फोटो व विकासकामे सादरीकरण",
                "जोशपूर्ण राजकीय व्हॉईसओव्हर व संवाद",
                "मोठ्या स्क्रीन व डिजिटल प्रचार योग्य फॉर्मॅट"
            ),
            addons = listOf(
                MarketplaceAddon("add_banner_elec", "सोबत ५ सोशल मीडिया प्रचार पोस्टर्स", 999, "+₹९९९")
            ),
            isPopular = true,
            badgeText = "🗳️ ELECTION SPECIAL",
            iconType = "video"
        ),

        // ==================== 2. AUDIO SERVICES ====================
        MarketplaceService(
            id = "srv_aud_01",
            serviceCode = "AUD-FM-LOUDSPEAKER",
            title = "FM Radio & Loudspeaker Ad Jingle",
            marathiTitle = "एफएम व रिक्षा लाउडस्पीकर ऑडिओ जाहिरात",
            category = MarketplaceCategory.AUDIO,
            basePrice = "₹१,९९९",
            priceNumber = 1999,
            deliveryTime = "२४ तास",
            shortDescription = "रिक्षा लाउडस्पीकर व रेडिओसाठी खणखणीत ऑडिओ जाहिरात. स्पष्ट व्हॉईसओव्हर आणि हाय-बास साऊंड मिक्सिंग.",
            deliverables = listOf(
                "३० ते ६० सेकंद व्यावसायिक ऑडिओ मास्टर",
                "स्थानिक ढोल, तुतारी किंवा मॉडर्न बीट्स",
                "वाहनांच्या लाउडस्पीकरवर न फाटणारा क्रिस्टल क्लिअर आवाज",
                "WAV & MP3 (320kbps) हाय-क्वालिटी फॉर्मॅट"
            ),
            addons = listOf(
                MarketplaceAddon("add_extra_voice", "दोन आवाजांमध्ये संवाद (Male + Female VO)", 599, "+₹५९९"),
                MarketplaceAddon("add_hindi_trans", "हिंदी भाषेत रूपांतर", 499, "+₹४९९")
            ),
            isPopular = true,
            badgeText = "🔊 TOP HIT",
            iconType = "audio"
        ),
        MarketplaceService(
            id = "srv_aud_02",
            serviceCode = "AUD-BRAND-THEME-SONG",
            title = "Custom Brand Theme Song & Melody (ब्रँड गाणे)",
            marathiTitle = "दुकान / ब्रँडचे खास संगीत व गाणे",
            category = MarketplaceCategory.AUDIO,
            basePrice = "₹३,९९९",
            priceNumber = 3999,
            deliveryTime = "४८ तास",
            shortDescription = "तुमच्या दुकानाच्या किंवा कंपनीच्या नावाचे स्वतंत्र गाणे. ओरिजिनल गीतलेखन आणि गायक कलाकारांचा आवाज.",
            deliverables = listOf(
                "विशेष मराठी / हिंदी गीत व चाल (Original Composition)",
                "व्यावसायिक गायकाचा आवाज व कोरस",
                "कॉपीराइट मुक्त सर्व हक्क (Full Commercial Rights)"
            ),
            addons = listOf(
                MarketplaceAddon("add_tune_caller", "कॉलरट्यून योग्य ३० सेकंद कट", 499, "+₹४९९")
            ),
            isPopular = false,
            badgeText = "🎵 ORIGINAL MUSIC",
            iconType = "audio"
        ),
        MarketplaceService(
            id = "srv_aud_03",
            serviceCode = "AUD-STUDIO-VOICEOVER",
            title = "Professional Studio Voiceover & Dubbing",
            marathiTitle = "स्टुडिओ निवेदन व व्हॉईसओव्हर",
            category = MarketplaceCategory.AUDIO,
            basePrice = "₹१,२९९",
            priceNumber = 1299,
            deliveryTime = "१२ ते २४ तास",
            shortDescription = "डॉक्युमेंटरी, ई-लर्निंग, आयव्हीआर (IVR) किंवा जाहिरातीसाठी शुद्ध उच्चारातील स्टुडिओ व्हॉईसओव्हर.",
            deliverables = listOf(
                "नॉईज-फ्री अॅकोस्टिक स्टुडिओ रेकॉर्डिंग",
                "मराठी, हिंदी, इंग्रजी भाषा पर्याय",
                "स्क्रिप्ट प्रूफरीडिंग व अचूक उच्चार"
            ),
            addons = listOf(
                MarketplaceAddon("add_ivr_telecom", "IVR टेलिकॉम सिस्टीम फॉर्मॅट", 399, "+₹३९९")
            ),
            isPopular = false,
            badgeText = "🎙️ STUDIO PRO",
            iconType = "audio"
        ),

        // ==================== 3. BANNER SERVICES ====================
        MarketplaceService(
            id = "srv_ban_01",
            serviceCode = "BAN-3D-METALLIC-LOGO",
            title = "3D Metallic & Gold Luxury Logo Design",
            marathiTitle = "३D गोल्ड व मेटॅलिक लोगो डिझाइन",
            category = MarketplaceCategory.BANNER,
            basePrice = "₹१,४९९",
            priceNumber = 1499,
            deliveryTime = "२४ तास",
            shortDescription = "दुकान, फर्म किंवा संस्थेसाठी आकर्षक ३D गोल्ड / सिल्व्हर मेटॅलिक लोगो. सर्व सोशल मीडिया व प्रिंटसाठी रेडी.",
            deliverables = listOf(
                "३D मेटॅलिक टेक्सचर लोगो (Gold, Silver, Bronze, Royal)",
                "प्रिंटसाठी मूळ व्हेक्टर फाईल्स (CorelDraw CDR, PSD, AI, PDF)",
                "पारदर्शक बॅकग्राउंड PNG (लेटरहेड व बिलासाठी)",
                "व्हॉट्सॲप डीपी व सोशल मीडिया प्रोफाईल फॉर्मॅट"
            ),
            addons = listOf(
                MarketplaceAddon("add_visiting_card", "व्हिजिटिंग कार्ड डिझाइन (Print Ready)", 499, "+₹४९९"),
                MarketplaceAddon("add_letterhead", "लेटरहेड + बिल बुक डिझाइन", 499, "+₹४९९")
            ),
            isPopular = true,
            badgeText = "✨ 3D LUXURY",
            iconType = "banner"
        ),
        MarketplaceService(
            id = "srv_ban_02",
            serviceCode = "BAN-SHOP-FLEX-HOARDING",
            title = "Shop & Showroom Flex Hoarding Board",
            marathiTitle = "दुकान व शोरूम फ्लेक्स होर्डिंग डिझाइन",
            category = MarketplaceCategory.BANNER,
            basePrice = "₹९९९",
            priceNumber = 999,
            deliveryTime = "१२ तास",
            shortDescription = "कोणत्याही साईजचे (उदा. १०x४, २०x१० फूट) दुकान बोर्ड व रस्त्यावरील होर्डिंगचे हाय-रिझोल्यूशन डिझाइन.",
            deliverables = listOf(
                "300 DPI अल्ट्रा शार्प प्रिंट रेडी फाईल",
                "उत्कृष्ट प्रॉडक्ट फोटो मांडणी व स्पष्ट फॉन्ट",
                "प्रिंटिंग प्रेससाठी योग्य CMYK कलर प्रोफाइल"
            ),
            addons = listOf(
                MarketplaceAddon("add_standee_design", "रोल-अप स्टॅन्डी डिझाइन (Roll-up Standee)", 499, "+₹४९९")
            ),
            isPopular = true,
            badgeText = "🏪 FAST PRINT",
            iconType = "banner"
        ),
        MarketplaceService(
            id = "srv_ban_03",
            serviceCode = "BAN-FESTIVAL-BUNDLE",
            title = "Festival & Promotional Offer Banner Pack",
            marathiTitle = "सण उत्सव व डिस्काउंट ऑफर बॅनर्स (५ पॅक)",
            category = MarketplaceCategory.BANNER,
            basePrice = "₹१,१९९",
            priceNumber = 1199,
            deliveryTime = "२४ तास",
            shortDescription = "गुढीपाडवा, दिवाळी, दसरा, ईद, स्वातंत्र्यदिन इत्यादी सणांसाठी तुमच्या ब्रँडिंगसह ५ कस्टमाईज्ड बॅनर्स.",
            deliverables = listOf(
                "५ सणांचे प्रीमियम डिझाइन बॅनर्स",
                "तुमचा लोगो, फोटो व मोबाईल नंबरसह कस्टमायझेशन",
                "व्हॉट्सॲप स्टेटस व इन्स्टाग्राम स्क्वेअर फॉर्मॅट"
            ),
            addons = listOf(
                MarketplaceAddon("add_extra_festivals", "अतिरिक्त ५ सण बॅनर्स (एकूण १०)", 899, "+₹८९९")
            ),
            isPopular = false,
            badgeText = "🎉 FESTIVE PACK",
            iconType = "banner"
        ),

        // ==================== 4. AI VIDEO SERVICES ====================
        MarketplaceService(
            id = "srv_ai_01",
            serviceCode = "AI-NEWS-ANCHOR-BULLETIN",
            title = "AI Virtual News Anchor Bulletin Ad",
            marathiTitle = "AI व्हर्च्युअल न्यूज अँकर बुलेटिन जाहिरात",
            category = MarketplaceCategory.AI_VIDEO,
            basePrice = "₹२,४९९",
            priceNumber = 2499,
            deliveryTime = "१२ तास",
            shortDescription = "अल्ट्रा-रिअलिस्टिक AI न्यूज अँकर तुमच्या दुकानाची किंवा उत्पादनाची बातमी सांगणारी 'ब्रेकिंग न्यूज' स्टाईल जाहिरात.",
            deliverables = listOf(
                "AI न्यूज अँकर (पुरुष / महिला) अचूक मराठी लिप-सिंकसह",
                "न्यूज चॅनल ग्राफिक्स, ब्रेकिंग न्यूज स्क्रोलर व टीकर",
                "उत्पादनाचे फोटो व व्हिडिओ इन्सर्ट्स (Picture-in-Picture)",
                "अत्यंत विश्वासार्ह व व्हायरल होणारे फॉरमॅट"
            ),
            addons = listOf(
                MarketplaceAddon("add_custom_avatar", "तुमच्या स्वतःच्या फोटोचा AI अवतार", 1499, "+₹१,४९९"),
                MarketplaceAddon("add_ai_script", "AI द्वारे हाय-कन्व्हर्टिंग स्क्रिप्ट तयार करणे", 399, "+₹३९९")
            ),
            isPopular = true,
            badgeText = "🤖 AI BREAKTHROUGH",
            iconType = "ai"
        ),
        MarketplaceService(
            id = "srv_ai_02",
            serviceCode = "AI-MULTILINGUAL-DUBBING",
            title = "Multilingual AI Voice Dubbing & Speech",
            marathiTitle = "AI बहुभाषिक व्हॉईस व डबिंग",
            category = MarketplaceCategory.AI_VIDEO,
            basePrice = "₹९९९",
            priceNumber = 999,
            deliveryTime = "६ तास",
            shortDescription = "मराठी, हिंदी, इंग्रजी, गुजराती, कन्नड भाषांमध्ये नैसर्गिक AI आवाजात जाहिरात रूपांतर.",
            deliverables = listOf(
                "नैसर्गिक हावभाव व भावनिक चढउतार असलेला AI आवाज",
                "एकाच स्क्रिप्टचे २ वेगवेगळ्या भाषांमध्ये व्हर्जन",
                "अतिजलद ६ तासांत डिलिव्हरी"
            ),
            addons = listOf(
                MarketplaceAddon("add_lang_3", "३ री भाषा अतिरिक्त जोडा", 499, "+₹४९९")
            ),
            isPopular = false,
            badgeText = "⚡ 6-HOUR DELIVERY",
            iconType = "ai"
        ),
        MarketplaceService(
            id = "srv_ai_03",
            serviceCode = "AI-TALKING-PHOTO-COMMERCIAL",
            title = "AI Talking Photo & Digital Character Video",
            marathiTitle = "AI टॉकिंग फोटो व डिजिटल कॅरेक्टर जाहिरात",
            category = MarketplaceCategory.AI_VIDEO,
            basePrice = "₹१,७९९",
            priceNumber = 1799,
            deliveryTime = "१२ तास",
            shortDescription = "तुमचा किंवा कोणत्याही मॉडेलचा स्थिर फोटो बोलणारा बनवा! रिअल-टाइम लिप मुव्हमेंटसह प्रभावी संदेश.",
            deliverables = listOf(
                "कोणत्याही हाय-क्वालिटी फोटोला बोलणारा व्हिडिओ बनवणे",
                "मागे 3D स्टुडिओ बॅकग्राउंड इफेक्ट",
                "सोशल मीडिया रील्स रेडी"
            ),
            addons = listOf(
                MarketplaceAddon("add_dynamic_bg", "कस्टम मोशन बॅकग्राउंड", 499, "+₹४९९")
            ),
            isPopular = false,
            badgeText = "🌟 AI AVATAR",
            iconType = "ai"
        ),

        // ==================== 5. SOCIAL MEDIA SERVICES ====================
        MarketplaceService(
            id = "srv_soc_01",
            serviceCode = "SOC-30-DAY-GROWTH",
            title = "30-Day Complete Social Media Growth Package",
            marathiTitle = "३० दिवसांचे सोशल मीडिया ग्रोथ बंडल",
            category = MarketplaceCategory.SOCIAL_MEDIA,
            basePrice = "₹६,९९९",
            priceNumber = 6999,
            deliveryTime = "मासिक (Monthly Package)",
            shortDescription = "तुमच्या व्यवसायाचे पूर्ण १ महिन्याचे सोशल मीडिया हँडलिंग. दररोजची पोस्ट्स, रील्स आणि कस्टमर एंगेजमेंट.",
            deliverables = listOf(
                "२० हाय-क्वालिटी ब्रँडेड ग्राफिक्स पोस्ट्स",
                "६ प्रोफेशनल इन्स्टाग्राम रील्स व्हिडिओ",
                "दर आठवड्याला ट्रेंडिंग हॅशटॅग्स व कॅप्शन्स",
                "फेस्टिव्हल आणि ऑफर बॅनर्स",
                "मासिक परफॉर्मन्स रिपोर्ट"
            ),
            addons = listOf(
                MarketplaceAddon("add_fb_ads_manage", "फेसबुक / इन्स्टा पेड अ‍ॅड्स सेटअप", 1999, "+₹१,९९९")
            ),
            isPopular = true,
            badgeText = "🚀 FULL MONTH BUNDLE",
            iconType = "social"
        ),
        MarketplaceService(
            id = "srv_soc_02",
            serviceCode = "SOC-REELS-POSTS-COMBO",
            title = "Instagram Boost: 10 Reels + 10 Graphics Combo",
            marathiTitle = "१० रील्स + १० पोस्ट्स इन्स्टाग्राम बूस्ट कॉम्बो",
            category = MarketplaceCategory.SOCIAL_MEDIA,
            basePrice = "३,९९९",
            priceNumber = 3999,
            deliveryTime = "१५ दिवस",
            shortDescription = "फॉलोअर्स आणि ग्राहकांपर्यंत पोहोचण्यासाठी १० आकर्षक रील्स आणि १० व्हेक्टर पोस्टर्सचा कॉम्बो पॅक.",
            deliverables = listOf(
                "१० व्हर्टिकल 9:16 रील्स व्हिडिओ (स्क्रिप्ट + एडिट)",
                "१० स्क्वेअर 1:1 HD प्रॉडक्ट / ऑफर पोस्टर्स",
                "ब्रँड लोगो वॉटरमार्क व पत्ता समाविष्ट"
            ),
            addons = listOf(
                MarketplaceAddon("add_story_templates", "५ मोफत व्हॉट्सॲप स्टोरी टेम्पलेट्स", 499, "+₹४९९")
            ),
            isPopular = true,
            badgeText = "🔥 VALUE PACK",
            iconType = "social"
        ),
        MarketplaceService(
            id = "srv_soc_03",
            serviceCode = "SOC-WHATSAPP-BROADCAST-KIT",
            title = "WhatsApp Business Marketing & Broadcast Kit",
            marathiTitle = "व्हॉट्सॲप बिझनेस मार्केटिंग व ब्रॉडकास्ट किट",
            category = MarketplaceCategory.SOCIAL_MEDIA,
            basePrice = "₹१,४९९",
            priceNumber = 1499,
            deliveryTime = "२४ तास",
            shortDescription = "ग्राहकांना पाठवण्यासाठी आकर्षक प्रॉडक्ट कॅटलॉग, पीडीएफ फ्लायर आणि १० ब्रॉडकास्ट संदेश डिझाइन्स.",
            deliverables = listOf(
                "१० हाय-रिझोल्यूशन व्हॉट्सॲप फॉरवर्डिंग बॅनर्स",
                "१ डिजिटल प्रॉडक्ट ब्राऊझर / पीडीएफ कॅटलॉग",
                "प्रभावी मराठी मेसेज कॉपीरायटिंग"
            ),
            addons = listOf(
                MarketplaceAddon("add_qr_card", "डिजिटल व्हिजिटिंग कार्ड (QR Code सह)", 499, "+₹४९९")
            ),
            isPopular = false,
            badgeText = "📱 WHATSAPP KIT",
            iconType = "social"
        )
    )
}
