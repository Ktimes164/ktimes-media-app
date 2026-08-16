package com.example.data

object DefaultData {
    val categories = listOf(
        "All",
        "ऑडिओ जाहिराती",
        "व्हिडिओ जाहिराती",
        "ग्राफिक्स डिझाइन",
        "निवडणूक स्पेशल",
        "अॅलबम गाणी",
        "Audio Ads",
        "Video Ads",
        "Graphics & Design",
        "Election Special"
    )

    val sampleItems = listOf(
        MediaItem(
            id = 1,
            sampleId = "KTM-ELE-2026",
            title = "निवडणूक प्रचार ऑडिओ २०२६ - विजय रथ (Election Prachar)",
            category = "निवडणूक स्पेशल",
            mediaType = "AUDIO",
            mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
            thumbnailUrl = "https://picsum.photos/seed/ktm_election_2026/600/400",
            description = "लाउडस्पीकर ऑटो व वाहनांसाठी खणखणीत आवाजात राजकीय प्रचार ऑडिओ रेकॉर्डिंग. स्थानिक लयबद्ध ढोल-ताशा बीट्स आणि प्रभावी शब्दरचना.",
            priceOrEstimate = "₹२,४९९ पासून",
            tags = "निवडणूक स्पेशल, Election Special, Prachar Audio, 2026, Campaign",
            isFeatured = true,
            whatsappMsg = "नमस्कार Ktimes Media, मला 'निवडणूक प्रचार ऑडिओ २०२६ (KTM-ELE-2026)' या सॅम्पलची माहिती हवी आहे."
        ),
        MediaItem(
            id = 2,
            sampleId = "KTM-VID-PROMO",
            title = "केटाईम्स मीडिया एजन्सी प्रोमो (HD Commercial)",
            category = "व्हिडिओ जाहिराती",
            mediaType = "VIDEO",
            mediaUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            thumbnailUrl = "https://picsum.photos/seed/ktm_promo_video/600/400",
            description = "4K अल्ट्रा HD डिजिटल प्रोमो व्हिडिओ. मोशन ग्राफिक्स, व्हॉईसओव्हर आणि स्टुडिओ साउंड मिक्सिंगसह व्यवसायाची दमदार जाहिरात.",
            priceOrEstimate = "₹४,९९९ पासून",
            tags = "व्हिडिओ जाहिराती, Video Ads, Agency Promo, 4K, Motion Graphics",
            isFeatured = true,
            whatsappMsg = "नमस्कार Ktimes Media, मला 'केटाईम्स मीडिया एजन्सी प्रोमो (KTM-VID-PROMO)' बद्दल चौकशी करायची आहे."
        ),
        MediaItem(
            id = 3,
            sampleId = "KTM-AUD-101",
            title = "ज्वेलरी स्टोअर सण उत्सव एफएम जाहिरात (Festive Ad)",
            category = "ऑडिओ जाहिराती",
            mediaType = "AUDIO",
            mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
            thumbnailUrl = "https://picsum.photos/seed/ktm_audio_1/600/400",
            description = "दिवाळी आणि सणासुदीच्या प्रसंगासाठी खास एफएम रेडिओ दर्जाची ऑडिओ जाहिरात. आकर्षक पार्श्वसंगीत आणि स्पष्ट व्हॉईसओव्हर.",
            priceOrEstimate = "₹१,९९९ पासून",
            tags = "ऑडिओ जाहिराती, FM Radio, Festive, Voiceover, Audio Ads",
            isFeatured = true,
            whatsappMsg = "नमस्कार Ktimes Media, मला 'ज्वेलरी स्टोअर सण उत्सव जाहिरात (KTM-AUD-101)' मागवायची आहे."
        ),
        MediaItem(
            id = 4,
            sampleId = "KTM-GRP-401",
            title = "ब्रांड आयडेंटिटी व निवडणूक पोस्टर डिझाइन (Poster Bundle)",
            category = "ग्राफिक्स डिझाइन",
            mediaType = "GRAPHIC",
            mediaUrl = "https://picsum.photos/seed/ktm_graphic_1/800/600",
            thumbnailUrl = "https://picsum.photos/seed/ktm_graphic_1/600/400",
            description = "हाय-रेजोल्यूशन व्हेक्टर पोस्टर्स, फ्लेक्स बॅनर्स, सोशल मीडिया फ्लाईयर्स आणि ३D लोगो डिझाइन पॅकेज.",
            priceOrEstimate = "₹९९९ पासून",
            tags = "ग्राफिक्स डिझाइन, Graphics & Design, Banner, Logo, Poster Design",
            isFeatured = true,
            whatsappMsg = "नमस्कार Ktimes Media, मला 'ब्रांड आयडेंटिटी व पोस्टर डिझाइन (KTM-GRP-401)' बद्दल माहिती द्या."
        ),
        MediaItem(
            id = 5,
            sampleId = "KTM-VID-201",
            title = "रियल इस्टेट प्रोजेक्ट टीव्ही कमर्शियल (Real Estate TVC)",
            category = "व्हिडिओ जाहिराती",
            mediaType = "VIDEO",
            mediaUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            thumbnailUrl = "https://picsum.photos/seed/ktm_video_1/600/400",
            description = "सिनेमॅटिक 4K प्रोमो व्हिडिओ. ३D ड्रोन व्ह्यूज, स्टुडिओ निवेदन आणि मोशन टायपोग्राफीसह भव्य प्रोजेक्ट सादरीकरण.",
            priceOrEstimate = "₹४,९९९ पासून",
            tags = "व्हिडिओ जाहिराती, TVC, 4K Video, Real Estate, Motion Graphics",
            isFeatured = true,
            whatsappMsg = "नमस्कार Ktimes Media, मला रियल इस्टेट टीव्ही कमर्शियल व्हिडिओ बनवायचा आहे."
        ),
        MediaItem(
            id = 6,
            sampleId = "KTM-ELE-301",
            title = "उमेदवार प्रचार गीत - संकल्प पत्र (Campaign Song)",
            category = "निवडणूक स्पेशल",
            mediaType = "AUDIO",
            mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
            thumbnailUrl = "https://picsum.photos/seed/ktm_election_1/600/400",
            description = "उमेदवाराचे नाव व निशाणीसह खास तयार केलेले धडाकेबाज गाणे. ब्रास म्युझिक आणि प्रेरणादायी शब्द.",
            priceOrEstimate = "₹२,४९९ पासून",
            tags = "निवडणूक स्पेशल, Prachar Audio, Election Song, Loudspeaker, Campaign",
            isFeatured = true,
            whatsappMsg = "नमस्कार Ktimes Media, मला उमेदवार प्रचार गीताचा सॅम्पल आवडला आहे."
        ),
        MediaItem(
            id = 7,
            sampleId = "KTM-ALB-501",
            title = "भक्ती संगीत ट्रॅक - शिव महिमा (Devotional Audio Track)",
            category = "अॅलबम गाणी",
            mediaType = "AUDIO",
            mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
            thumbnailUrl = "https://picsum.photos/seed/ktm_devotional_1/600/400",
            description = "लाइव्ह तबला, बासरी आणि स्टुडिओ व्हॉईस मिक्सींगसह बनवलेले भक्तिगीत. यूट्यूब रिलीझसाठी उत्तम.",
            priceOrEstimate = "₹३,४९९ पासून",
            tags = "अॅलबम गाणी, Devotional, Regional, Vocal Mixing, Album Track",
            isFeatured = false,
            whatsappMsg = "नमस्कार Ktimes Media, मला भक्तिगीत रेकॉर्डिंगची माहिती हवी आहे."
        )
    )

    val businessClubTestimonials = listOf(
        com.example.data.models.Testimonial(
            id = "test-1",
            clientName = "गणेश सराफ",
            businessName = "गणेश ज्वेलर्स अँड सन्स",
            businessType = "सुवर्ण व रत्न व्यवसाय • कोल्हापूर",
            rating = 5,
            feedback = "Ktimes Media च्या ऑडिओ जाहिरातीमुळे आमच्या दिवाळी आणि गुढीपाडवा उत्सवात ग्राहकांची प्रचंड गर्दी झाली. खणखणीत आवाज, वेळेत डिलिव्हरी आणि दर्जेदार मिक्सिंग!",
            serviceUsed = "FM रेडिओ ऑडिओ जाहिरात",
            cityOrRegion = "कोल्हापूर / सांगली",
            avatarInitials = "ग",
            verifiedMember = true,
            dateText = "ऑगस्ट २०२६"
        ),
        com.example.data.models.Testimonial(
            id = "test-2",
            clientName = "संजय देशपांडे",
            businessName = "साई रियाल्टीज् & डेव्हलपर्स",
            businessType = "रियल इस्टेट व कन्स्ट्रक्शन • पुणे",
            rating = 5,
            feedback = "आमच्या नव्या लक्झरी गृहप्रकल्पाचा 4K सिनेमॅटिक प्रोमो व्हिडिओ Ktimes ने अवघ्या २४ तासांत तयार करून दिला. सोशल मीडियावर ३ लाखांहून अधिक स्थानिक व्ह्यूज मिळाले.",
            serviceUsed = "4K TVC व्हिडिओ प्रोमो",
            cityOrRegion = "पुणे",
            avatarInitials = "स",
            verifiedMember = true,
            dateText = "ऑगस्ट २०२६"
        ),
        com.example.data.models.Testimonial(
            id = "test-3",
            clientName = "ॲड. नितीन शिंदे",
            businessName = "महालक्ष्मी व्यापारी असोसिएशन",
            businessType = "व्यापारी मंडळ & राजकीय मंच • सातारा",
            rating = 5,
            feedback = "निवडणूक प्रचार सभा आणि महाअधिवेशनासाठी बनवलेले धडाकेबाज जिंन्गल्स आणि ३D बॅनर्स अत्यंत प्रभावी ठरले. सर्व सभासदांनी खूप कौतुक केले.",
            serviceUsed = "निवडणूक प्रचार स्पेशल जिंन्गल",
            cityOrRegion = "सातारा",
            avatarInitials = "नि",
            verifiedMember = true,
            dateText = "जुलै २०२६"
        ),
        com.example.data.models.Testimonial(
            id = "test-4",
            clientName = "डॉ. प्रियांका जोशी",
            businessName = "संजीवनी मल्टिस्पेशालिटी क्लिनिक",
            businessType = "आरोग्य सेवा & वेलनेस • नाशिक",
            rating = 5,
            feedback = "AI व्हिडिओ न्यूज अँकर आणि व्हॉट्सॲप ऑडिओमुळे क्लिनिकमध्ये नवीन रुग्णांची नोंदणी दुप्पट झाली. दर्जेदार सादरीकरण आणि उत्तम सपोर्ट!",
            serviceUsed = "AI व्हिडिओ अँकर & सोशल बंडल",
            cityOrRegion = "नाशिक",
            avatarInitials = "प्रि",
            verifiedMember = true,
            dateText = "ऑगस्ट २०२६"
        ),
        com.example.data.models.Testimonial(
            id = "test-5",
            clientName = "विकास पाटील",
            businessName = "पाटील ॲग्रोटेक & सीड्स",
            businessType = "कृषी उत्पादने व बियाणे • सोलापूर",
            rating = 5,
            feedback = "ग्रामीण भागातील शेतकऱ्यांपर्यंत पोहोचण्यासाठी लाउडस्पीकर ऑटो ऑडिओ कॅम्पेन सर्वोत्तम ठरले. Ktimes Media मुळे ब्रँडची विश्वासार्हता वाढली.",
            serviceUsed = "ऑटो लाउडस्पीकर ऑडिओ",
            cityOrRegion = "सोलापूर / धाराशिव",
            avatarInitials = "वि",
            verifiedMember = true,
            dateText = "जुलै २०२६"
        )
    )
}
