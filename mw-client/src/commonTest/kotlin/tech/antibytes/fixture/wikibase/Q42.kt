/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.fixture.wikibase

import kotlinx.datetime.Instant
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.wikibase.model.Entity
import tech.antibytes.mediawiki.wikibase.model.LanguageValuePair

internal val q42 = Entity(
    id = "Q42",
    revisionId = 1558961212,
    lastModification = Instant.parse("2022-01-09T05:52:11Z"),
    type = DataModelContract.EntityType.ITEM,
    labels = mapOf(
        "en" to LanguageValuePair(language = "en", value = "Douglas Adams"),
        "fr" to LanguageValuePair(language = "fr", value = "Douglas Adams"),
        "ru" to LanguageValuePair(language = "ru", value = "Дуглас Адамс"),
        "pl" to LanguageValuePair(language = "pl", value = "Douglas Adams"),
        "it" to LanguageValuePair(language = "it", value = "Douglas Adams"),
        "en-gb" to LanguageValuePair(language = "en-gb", value = "Douglas Adams"),
        "nb" to LanguageValuePair(language = "nb", value = "Douglas Adams"),
        "es" to LanguageValuePair(language = "es", value = "Douglas Adams"),
        "en-ca" to LanguageValuePair(language = "en-ca", value = "Douglas Adams"),
        "hr" to LanguageValuePair(language = "hr", value = "Douglas Adams"),
        "pt" to LanguageValuePair(language = "pt", value = "Douglas Adams"),
        "ko" to LanguageValuePair(language = "ko", value = "더글러스 애덤스"),
        "nl" to LanguageValuePair(language = "nl", value = "Douglas Adams"),
        "el" to LanguageValuePair(language = "el", value = "Ντάγκλας Άνταμς"),
        "ar" to LanguageValuePair(language = "ar", value = "دوغلاس آدمز"),
        "arz" to LanguageValuePair(language = "arz", value = "دوجلاس ادامز"),
        "bar" to LanguageValuePair(language = "bar", value = "Douglas Adams"),
        "be" to LanguageValuePair(language = "be", value = "Дуглас Адамс"),
        "bg" to LanguageValuePair(language = "bg", value = "Дъглас Адамс"),
        "bs" to LanguageValuePair(language = "bs", value = "Douglas Adams"),
        "ca" to LanguageValuePair(language = "ca", value = "Douglas Adams"),
        "cs" to LanguageValuePair(language = "cs", value = "Douglas Adams"),
        "cy" to LanguageValuePair(language = "cy", value = "Douglas Adams"),
        "da" to LanguageValuePair(language = "da", value = "Douglas Adams"),
        "eo" to LanguageValuePair(language = "eo", value = "Douglas Adams"),
        "et" to LanguageValuePair(language = "et", value = "Douglas Adams"),
        "fa" to LanguageValuePair(language = "fa", value = "داگلاس آدامز"),
        "fi" to LanguageValuePair(language = "fi", value = "Douglas Adams"),
        "ga" to LanguageValuePair(language = "ga", value = "Douglas Adams"),
        "gl" to LanguageValuePair(language = "gl", value = "Douglas Adams"),
        "he" to LanguageValuePair(language = "he", value = "דאגלס אדמס"),
        "hu" to LanguageValuePair(language = "hu", value = "Douglas Adams"),
        "id" to LanguageValuePair(language = "id", value = "Douglas Adams"),
        "io" to LanguageValuePair(language = "io", value = "Douglas Adams"),
        "is" to LanguageValuePair(language = "is", value = "Douglas Adams"),
        "ja" to LanguageValuePair(language = "ja", value = "ダグラス・アダムズ"),
        "jv" to LanguageValuePair(language = "jv", value = "Douglas Adams"),
        "ka" to LanguageValuePair(language = "ka", value = "დაგლას ადამსი"),
        "la" to LanguageValuePair(language = "la", value = "Duglassius Adams"),
        "lv" to LanguageValuePair(language = "lv", value = "Duglass Adamss"),
        "mk" to LanguageValuePair(language = "mk", value = "Даглас Адамс"),
        "mr" to LanguageValuePair(language = "mr", value = "डग्लस अॅडम्स"),
        "nn" to LanguageValuePair(language = "nn", value = "Douglas Adams"),
        "ro" to LanguageValuePair(language = "ro", value = "Douglas Adams"),
        "sco" to LanguageValuePair(language = "sco", value = "Douglas Adams"),
        "sh" to LanguageValuePair(language = "sh", value = "Douglas Adams"),
        "sk" to LanguageValuePair(language = "sk", value = "Douglas Adams"),
        "sl" to LanguageValuePair(language = "sl", value = "Douglas Adams"),
        "sq" to LanguageValuePair(language = "sq", value = "Douglas Adams"),
        "sr" to LanguageValuePair(language = "sr", value = "Даглас Адамс"),
        "sv" to LanguageValuePair(language = "sv", value = "Douglas Adams"),
        "ta" to LanguageValuePair(language = "ta", value = "டக்ளஸ் ஆடம்ஸ்"),
        "tr" to LanguageValuePair(language = "tr", value = "Douglas Adams"),
        "uk" to LanguageValuePair(language = "uk", value = "Дуглас Адамс"),
        "vi" to LanguageValuePair(language = "vi", value = "Douglas Adams"),
        "zh" to LanguageValuePair(language = "zh", value = "道格拉斯·亞當斯"),
        "zh-cn" to LanguageValuePair(language = "zh-cn", value = "道格拉斯·亚当斯"),
        "zh-hans" to LanguageValuePair(language = "zh-hans", value = "道格拉斯·亚当斯"),
        "zh-hant" to LanguageValuePair(language = "zh-hant", value = "道格拉斯·亞當斯"),
        "de-ch" to LanguageValuePair(language = "de-ch", value = "Douglas Adams"),
        "pt-br" to LanguageValuePair(language = "pt-br", value = "Douglas Adams"),
        "zh-sg" to LanguageValuePair(language = "zh-sg", value = "道格拉斯·亚当斯"),
        "zh-my" to LanguageValuePair(language = "zh-my", value = "道格拉斯·亚当斯"),
        "zh-hk" to LanguageValuePair(language = "zh-hk", value = "道格拉斯·亞當斯"),
        "zh-tw" to LanguageValuePair(language = "zh-tw", value = "道格拉斯·亞當斯"),
        "zh-mo" to LanguageValuePair(language = "zh-mo", value = "道格拉斯·亞當斯"),
        "war" to LanguageValuePair(language = "war", value = "Douglas Adams"),
        "be-tarask" to LanguageValuePair(language = "be-tarask", value = "Дуглас Адамз"),
        "vep" to LanguageValuePair(language = "vep", value = "Adams Duglas"),
        "ur" to LanguageValuePair(language = "ur", value = "ڈگلس ایڈم"),
        "oc" to LanguageValuePair(language = "oc", value = "Douglas Adams"),
        "af" to LanguageValuePair(language = "af", value = "Douglas Adams"),
        "an" to LanguageValuePair(language = "an", value = "Douglas Adams"),
        "br" to LanguageValuePair(language = "br", value = "Douglas Adams"),
        "eu" to LanguageValuePair(language = "eu", value = "Douglas Adams"),
        "lb" to LanguageValuePair(language = "lb", value = "Douglas Adams"),
        "lmo" to LanguageValuePair(language = "lmo", value = "Douglas Adams"),
        "lt" to LanguageValuePair(language = "lt", value = "Douglas Adams"),
        "nds" to LanguageValuePair(language = "nds", value = "Douglas Adams"),
        "nds-nl" to LanguageValuePair(language = "nds-nl", value = "Douglas Adams"),
        "pms" to LanguageValuePair(language = "pms", value = "Douglas Adams"),
        "vec" to LanguageValuePair(language = "vec", value = "Douglas Adams"),
        "wa" to LanguageValuePair(language = "wa", value = "Douglas Adams"),
        "sr-ec" to LanguageValuePair(language = "sr-ec", value = "Даглас Адамс"),
        "sr-el" to LanguageValuePair(language = "sr-el", value = "Daglas Adams"),
        "de" to LanguageValuePair(language = "de", value = "Douglas Adams"),
        "ckb" to LanguageValuePair(language = "ckb", value = "دەگلاس ئادمز"),
        "fo" to LanguageValuePair(language = "fo", value = "Douglas Adams"),
        "kl" to LanguageValuePair(language = "kl", value = "Douglas Adams"),
        "gsw" to LanguageValuePair(language = "gsw", value = "Douglas Adams"),
        "te" to LanguageValuePair(language = "te", value = "డగ్లస్ ఆడమ్స్"),
        "si" to LanguageValuePair(language = "si", value = "ඩග්ලස් ඇඩම්ස්"),
        "bn" to LanguageValuePair(language = "bn", value = "ডগলাস অ্যাডামস"),
        "hi" to LanguageValuePair(language = "hi", value = "डग्लस अ‍डम्स"),
        "rwr" to LanguageValuePair(language = "rwr", value = "डग्लस अ‍डम्स"),
        "mg" to LanguageValuePair(language = "mg", value = "Douglas Adams"),
        "ml" to LanguageValuePair(language = "ml", value = "ഡഗ്ലസ് ആഡംസ്"),
        "gu" to LanguageValuePair(language = "gu", value = "ડગ્લાસ એડમ્સ"),
        "hy" to LanguageValuePair(language = "hy", value = "Դուգլաս Ադամս"),
        "ast" to LanguageValuePair(language = "ast", value = "Douglas Adams"),
        "co" to LanguageValuePair(language = "co", value = "Douglas Adams"),
        "de-at" to LanguageValuePair(language = "de-at", value = "Douglas Adams"),
        "frp" to LanguageValuePair(language = "frp", value = "Douglas Adams"),
        "fur" to LanguageValuePair(language = "fur", value = "Douglas Adams"),
        "gd" to LanguageValuePair(language = "gd", value = "Douglas Adams"),
        "ia" to LanguageValuePair(language = "ia", value = "Douglas Adams"),
        "ie" to LanguageValuePair(language = "ie", value = "Douglas Adams"),
        "kg" to LanguageValuePair(language = "kg", value = "Douglas Adams"),
        "li" to LanguageValuePair(language = "li", value = "Douglas Adams"),
        "lij" to LanguageValuePair(language = "lij", value = "Douglas Adams"),
        "min" to LanguageValuePair(language = "min", value = "Douglas Adams"),
        "ms" to LanguageValuePair(language = "ms", value = "Douglas Adams"),
        "nap" to LanguageValuePair(language = "nap", value = "Douglas Adams"),
        "nrm" to LanguageValuePair(language = "nrm", value = "Douglas Adams"),
        "pcd" to LanguageValuePair(language = "pcd", value = "Douglas Adams"),
        "rm" to LanguageValuePair(language = "rm", value = "Douglas Adams"),
        "sc" to LanguageValuePair(language = "sc", value = "Douglas Adams"),
        "scn" to LanguageValuePair(language = "scn", value = "Douglas Adams"),
        "sw" to LanguageValuePair(language = "sw", value = "Douglas Adams"),
        "vls" to LanguageValuePair(language = "vls", value = "Douglas Adams"),
        "vo" to LanguageValuePair(language = "vo", value = "Douglas Adams"),
        "wo" to LanguageValuePair(language = "wo", value = "Douglas Adams"),
        "zu" to LanguageValuePair(language = "zu", value = "Douglas Adams"),
        "az" to LanguageValuePair(language = "az", value = "Duqlas Noel Adams"),
        "ak" to LanguageValuePair(language = "ak", value = "Doglas Adams"),
        "or" to LanguageValuePair(language = "or", value = "ଡଗ୍\u200Cଲାସ୍\u200C ଆଦାମ୍\u200Cସ"),
        "kn" to LanguageValuePair(language = "kn", value = "ಡಗ್ಲಸ್ ಆಡಮ್ಸ್"),
        "ne" to LanguageValuePair(language = "ne", value = "डगलस एडम्स"),
        "mrj" to LanguageValuePair(language = "mrj", value = "Адамс"),
        "th" to LanguageValuePair(language = "th", value = "ดักลัส แอดัมส์"),
        "pa" to LanguageValuePair(language = "pa", value = "ਡਗਲਸ ਐਡਮਜ਼"),
        "tcy" to LanguageValuePair(language = "tcy", value = "ಡಾಗ್ಲಸ್ ಆಡಮ್ಸ್"),
        "tl" to LanguageValuePair(language = "tl", value = "Douglas Adams"),
        "ext" to LanguageValuePair(language = "ext", value = "Douglas Adams"),
        "azb" to LanguageValuePair(language = "azb", value = "داقلاس آدامز"),
        "lfn" to LanguageValuePair(language = "lfn", value = "Douglas Adams"),
        "nan" to LanguageValuePair(language = "nan", value = "Douglas Adams"),
        "ky" to LanguageValuePair(language = "ky", value = "Дуглас Адамс"),
        "bho" to LanguageValuePair(language = "bho", value = "डगलस एडम्स"),
        "wuu" to LanguageValuePair(language = "wuu", value = "道格拉斯·亚当斯"),
        "yue" to LanguageValuePair(language = "yue", value = "道格拉斯亞當斯"),
        "pnb" to LanguageValuePair(language = "pnb", value = "ڈگلس ایڈمس"),
        "sje" to LanguageValuePair(language = "sje", value = "Douglas Adams"),
        "se" to LanguageValuePair(language = "se", value = "Douglas Adams"),
        "smn" to LanguageValuePair(language = "smn", value = "Douglas Adams"),
        "sms" to LanguageValuePair(language = "sms", value = "Douglas Adams"),
        "sma" to LanguageValuePair(language = "sma", value = "Douglas Adams"),
        "smj" to LanguageValuePair(language = "smj", value = "Douglas Adams"),
        "bm" to LanguageValuePair(language = "bm", value = "Douglas Adams"),
        "frc" to LanguageValuePair(language = "frc", value = "Douglas Adams"),
        "jam" to LanguageValuePair(language = "jam", value = "Douglas Adams"),
        "kab" to LanguageValuePair(language = "kab", value = "Douglas Adams"),
        "pap" to LanguageValuePair(language = "pap", value = "Douglas Adams"),
        "prg" to LanguageValuePair(language = "prg", value = "Douglas Adams"),
        "rgn" to LanguageValuePair(language = "rgn", value = "Douglas Adams"),
        "vmf" to LanguageValuePair(language = "vmf", value = "Douglas Adams"),
        "nqo" to LanguageValuePair(language = "nqo", value = "ߘߎߜ߭ߑߟߊߛ ߊߘߊߡߛ"),
        "fy" to LanguageValuePair(language = "fy", value = "Douglas Adams"),
        "hsb" to LanguageValuePair(language = "hsb", value = "Douglas Adams"),
        "yi" to LanguageValuePair(language = "yi", value = "דאַגלאַס אַדאַמס"),
        "kw" to LanguageValuePair(language = "kw", value = "Douglas Adams"),
        "ms-arab" to LanguageValuePair(language = "ms-arab", value = "دݢلس ايدمﺯ"),
        "jut" to LanguageValuePair(language = "jut", value = "Douglas Adams")
    ),
    descriptions = mapOf(
        "en" to LanguageValuePair(language = "en", value = "English writer and humorist (1952-2001)"),
        "fr" to LanguageValuePair(language = "fr", value = "écrivain anglais de science-fiction"),
        "en-gb" to LanguageValuePair(language = "en-gb", value = "English writer and humourist"),
        "nb" to LanguageValuePair(language = "nb", value = "engelsk science fiction-forfatter og humorist"),
        "it" to LanguageValuePair(language = "it", value = "scrittore ed umorista britannico"),
        "de" to LanguageValuePair(language = "de", value = "britischer Schriftsteller"),
        "es" to LanguageValuePair(language = "es", value = "escritor y humorista británico"),
        "ru" to LanguageValuePair(language = "ru", value = "английский писатель, драматург и сценарист, автор серии книг «Автостопом по галактике»"),
        "zh-hans" to LanguageValuePair(language = "zh-hans", value = "英国作家"),
        "zh-hant" to LanguageValuePair(language = "zh-hant", value = "英國作家"),
        "zh-cn" to LanguageValuePair(language = "zh-cn", value = "英国作家"),
        "zh-sg" to LanguageValuePair(language = "zh-sg", value = "英国作家"),
        "zh-my" to LanguageValuePair(language = "zh-my", value = "英国作家"),
        "zh" to LanguageValuePair(language = "zh", value = "英國作家"),
        "zh-hk" to LanguageValuePair(language = "zh-hk", value = "英國作家"),
        "zh-tw" to LanguageValuePair(language = "zh-tw", value = "英國文學家、幽默作家"),
        "zh-mo" to LanguageValuePair(language = "zh-mo", value = "英國作家"),
        "ca" to LanguageValuePair(language = "ca", value = "escriptor anglès"),
        "fi" to LanguageValuePair(language = "fi", value = "englantilainen kirjailija ja humoristi"),
        "cs" to LanguageValuePair(language = "cs", value = "anglický spisovatel, humorista a dramatik"),
        "sv" to LanguageValuePair(language = "sv", value = "brittisk författare (1952–2001)"),
        "pt-br" to LanguageValuePair(language = "pt-br", value = "escritor e comediante britânico"),
        "ta" to LanguageValuePair(language = "ta", value = "ஆங்கில எழுத்தாளர் மற்றும் நகைச்சுவையாளர்"),
        "sl" to LanguageValuePair(language = "sl", value = "angleški pisatelj, humorist in dramatik"),
        "da" to LanguageValuePair(language = "da", value = "engelsk forfatter"),
        "nl" to LanguageValuePair(language = "nl", value = "Engelse schrijver (1952-2001)"),
        "pt" to LanguageValuePair(language = "pt", value = "escritor e comediante britânico"),
        "pl" to LanguageValuePair(language = "pl", value = "brytyjski pisarz"),
        "lv" to LanguageValuePair(language = "lv", value = "angļu zinātniskās fantastikas rakstnieks un humorists"),
        "sr" to LanguageValuePair(language = "sr", value = "енглески писац и хумориста"),
        "sr-ec" to LanguageValuePair(language = "sr-ec", value = "енглески писац научне фантастике и хумориста"),
        "sr-el" to LanguageValuePair(language = "sr-el", value = "engleski pisac naučne fantastike i humorista"),
        "eo" to LanguageValuePair(language = "eo", value = "angla aŭtoro de sciencfikcio-romanoj kaj humoristo"),
        "bar" to LanguageValuePair(language = "bar", value = "a englischer Science-Fiction-Schriftsteller"),
        "br" to LanguageValuePair(language = "br", value = "skrivagner saoznek"),
        "ja" to LanguageValuePair(language = "ja", value = "イングランドの作家"),
        "nn" to LanguageValuePair(language = "nn", value = "forfattar"),
        "tr" to LanguageValuePair(language = "tr", value = "İngiliz yazar"),
        "si" to LanguageValuePair(language = "si", value = "ඉංග්‍රීසි කවියෙක්"),
        "vi" to LanguageValuePair(language = "vi", value = "Nhà văn và nhà soạn hài kịch người Anh"),
        "cy" to LanguageValuePair(language = "cy", value = "awdur a dychanwr Seisnig"),
        "gu" to LanguageValuePair(language = "gu", value = "અંગ્રેજી લેખક અને હાસ્યકાર"),
        "uk" to LanguageValuePair(language = "uk", value = "британський комічний радіодраматург, письменник"),
        "ro" to LanguageValuePair(language = "ro", value = "scriitor, dramaturg englez"),
        "hu" to LanguageValuePair(language = "hu", value = "angol író"),
        "fa" to LanguageValuePair(language = "fa", value = "فیلمنامه‌نویس و نویسنده بریتانیایی"),
        "af" to LanguageValuePair(language = "af", value = "Engelse skrywer en humoris"),
        "mk" to LanguageValuePair(language = "mk", value = "англиски писател и хуморист"),
        "el" to LanguageValuePair(language = "el", value = "Άγγλος συγγραφέας"),
        "hy" to LanguageValuePair(language = "hy", value = "անգլիացի գրող, դրամատուրգ, սցենարիստ, «Ավտոստոպով զբոսաշրջիկի միջգալակտիկական ուղեցույց» վեպերի շարք"),
        "bg" to LanguageValuePair(language = "bg", value = "английски писател и хуморист"),
        "ne" to LanguageValuePair(language = "ne", value = "अङ्ग्रेजी लेखक र व्यङ्ग्यकार"),
        "he" to LanguageValuePair(language = "he", value = "סופר והומוריסטן בריטי"),
        "de-at" to LanguageValuePair(language = "de-at", value = "britischer Schriftsteller"),
        "de-ch" to LanguageValuePair(language = "de-ch", value = "britischer Schriftsteller"),
        "gsw" to LanguageValuePair(language = "gsw", value = "britischer Schriftsteller"),
        "nds" to LanguageValuePair(language = "nds", value = "englischer Schriftsteller"),
        "kn" to LanguageValuePair(language = "kn", value = "ಇಂಗ್ಲಿಷ್ ಭಾಷೆಯ ಬರಹಗಾರ ಹಾಗೂ ಹಾಸ್ಯ ಲೇಖಕ"),
        "pa" to LanguageValuePair(language = "pa", value = "ਅੰਗਰੇਜ਼ੀ ਲੇਖਕ"),
        "ar" to LanguageValuePair(language = "ar", value = "كاتب إنجليزي فكاهي"),
        "tl" to LanguageValuePair(language = "tl", value = "taga-Inglatera na manunulat at tagapagpatawa"),
        "eu" to LanguageValuePair(language = "eu", value = "idazle eta umorista britaniarra"),
        "hr" to LanguageValuePair(language = "hr", value = "britanski radijski dramaturg i pisac znanstvene fantastike"),
        "ko" to LanguageValuePair(language = "ko", value = "영국의 작가"),
        "sw" to LanguageValuePair(language = "sw", value = "mwandishi Mwingereza"),
        "th" to LanguageValuePair(language = "th", value = "นักเขียนและผู้เล่าเรื่องอารมณ์ขันชาวอังกฤษ"),
        "en-ca" to LanguageValuePair(language = "en-ca", value = "English writer"),
        "gd" to LanguageValuePair(language = "gd", value = "sgrìobhadair Sasannach"),
        "ka" to LanguageValuePair(language = "ka", value = "ინგლისელი მწერალი და იუმორისტი"),
        "et" to LanguageValuePair(language = "et", value = "inglise ulmekirjanik"),
        "te" to LanguageValuePair(language = "te", value = "ఇంగ్లీషు రచయిత, హాస్యకారుడు"),
        "ast" to LanguageValuePair(language = "ast", value = "escritor y humorista inglés"),
        "sq" to LanguageValuePair(language = "sq", value = "autor dhe humorist anglez"),
        "gl" to LanguageValuePair(language = "gl", value = "escritor e guionista británico"),
        "bho" to LanguageValuePair(language = "bho", value = "अंग्रेजी भाषा के ब्रिटिश लेखक"),
        "sk" to LanguageValuePair(language = "sk", value = "anglický spisovateľ"),
        "la" to LanguageValuePair(language = "la", value = "scriptor, scriptor scaenicus, et mythistoricus (1952–2001)"),
        "ml" to LanguageValuePair(language = "ml", value = "ബ്രിട്ടീഷ് എഴുത്തുകാരനും ഹാസ്യസാഹിത്യാകാരനും"),
        "ga" to LanguageValuePair(language = "ga", value = "scríbhneoir Sasanach"),
        "ku-latn" to LanguageValuePair(language = "ku-latn", value = "nivîskarê brîtanî"),
        "min" to LanguageValuePair(language = "min", value = "Panulih jo palawak dari Inggirih"),
        "hi" to LanguageValuePair(language = "hi", value = "अंग्रेजी लेखक"),
        "nqo" to LanguageValuePair(language = "nqo", value = "ߊ߲߬ߜ߭ߌ߬ߟߋ߬ ߛߓߍߦߟߊ ߞߎ߲߬ߘߐ߬ߕߍ߰ ߟߐ߲ߞߏ ߞߊ߲߬"),
        "be" to LanguageValuePair(language = "be", value = "англійскі пісьменнік"),
        "id" to LanguageValuePair(language = "id", value = "penulis Britania Raya"),
        "bs" to LanguageValuePair(language = "bs", value = "engleski pisac i humorist"),
        "ms" to LanguageValuePair(language = "ms", value = "penulis dan pelawak Inggeris"),
        "mr" to LanguageValuePair(language = "mr", value = "ब्रिटिश लेखक व नाटककार"),
        "bn" to LanguageValuePair(language = "bn", value = "ইংরেজ লেখক ও কৌতুকবিদ"),
        "dv" to LanguageValuePair(language = "dv", value = "ލިޔުންތެރިއެއް")
    ),
    aliases = mapOf(
        "en" to listOf(
            LanguageValuePair(language = "en", value = "Douglas Noel Adams"),
            LanguageValuePair(language = "en", value = "Douglas Noël Adams"),
            LanguageValuePair(language = "en", value = "Douglas N. Adams")
        ),
        "ru" to listOf(
            LanguageValuePair(language = "ru", value = "Адамс, Дуглас"),
            LanguageValuePair(language = "ru", value = "Дуглас Ноэль Адамс"),
            LanguageValuePair(language = "ru", value = "Адамс, Дуглас Ноэль")
        ),
        "nb" to listOf(
            LanguageValuePair(language = "nb", value = "Douglas Noël Adams"),
            LanguageValuePair(language = "nb", value = "Douglas N. Adams")
        ),
        "fr" to listOf(
            LanguageValuePair(language = "fr", value = "Douglas Noel Adams"),
            LanguageValuePair(language = "fr", value = "Douglas Noël Adams")
        ),
        "de" to listOf(
            LanguageValuePair(language = "de", value = "Douglas Noel Adams"),
            LanguageValuePair(language = "de", value = "Douglas Noël Adams")
        ),
        "pt-br" to listOf(
            LanguageValuePair(language = "pt-br", value = "Douglas Noël Adams"),
            LanguageValuePair(language = "pt-br", value = "Douglas Noel Adams")
        ),
        "be-tarask" to listOf(
            LanguageValuePair(language = "be-tarask", value = "Дуглас Адамс")
        ),
        "zh" to listOf(
            LanguageValuePair(language = "zh", value = "亞當斯")
        ),
        "es" to listOf(
            LanguageValuePair(language = "es", value = "Douglas Noel Adams"),
            LanguageValuePair(language = "es", value = "Douglas Noël Adams")
        ),
        "it" to listOf(
            LanguageValuePair(language = "it", value = "Douglas Noel Adams"),
            LanguageValuePair(language = "it", value = "Douglas N. Adams")
        ),
        "cs" to listOf(
            LanguageValuePair(language = "cs", value = "Douglas Noël Adams"),
            LanguageValuePair(language = "cs", value = "Douglas Noel Adams"),
            LanguageValuePair(language = "cs", value = "Douglas N. Adams")
        ),
        "hy" to listOf(
            LanguageValuePair(language = "hy", value = "Ադամս, Դուգլաս")
        ),
        "el" to listOf(
            LanguageValuePair(language = "el", value = "Ντάγκλας Νόελ Άνταμς")
        ),
        "nl" to listOf(
            LanguageValuePair(language = "nl", value = "Douglas Noel Adams"),
            LanguageValuePair(language = "nl", value = "Douglas Noël Adams")
        ),
        "pt" to listOf(
            LanguageValuePair(language = "pt", value = "Douglas Noël Adams"),
            LanguageValuePair(language = "pt", value = "Douglas Noel Adams")
        ),
        "ja" to listOf(
            LanguageValuePair(language = "ja", value = "ダグラス・アダムス")
        ),
        "pa" to listOf(
            LanguageValuePair(language = "pa", value = "ਡਗਲਸ ਨੋਏਲ ਐਡਮਜ਼"),
            LanguageValuePair(language = "pa", value = "ਡਗਲਸ ਐਡਮਸ")
        ),
        "tl" to listOf(
            LanguageValuePair(language = "tl", value = "Douglas Noël Adams"),
            LanguageValuePair(language = "tl", value = "Douglas Noel Adams")
        ),
        "eu" to listOf(
            LanguageValuePair(language = "eu", value = "Douglas Noel Adams"),
            LanguageValuePair(language = "eu", value = "Douglas Noël Adams")
        ),
        "uk" to listOf(
            LanguageValuePair(language = "uk", value = "Дуглас Ноел Адамс"),
            LanguageValuePair(language = "uk", value = "Адамс Дуглас")
        ),
        "hr" to listOf(
            LanguageValuePair(language = "hr", value = "Douglas Noël Adams"),
            LanguageValuePair(language = "hr", value = "Douglas N. Adams"),
            LanguageValuePair(language = "hr", value = "Douglas Noel Adams")
        ),
        "he" to listOf(
            LanguageValuePair(language = "he", value = "דגלס אדמס"),
            LanguageValuePair(language = "he", value = "דאגלס נואל אדמס")
        ),
        "ko" to listOf(
            LanguageValuePair(language = "ko", value = "더글라스 애덤스"),
            LanguageValuePair(language = "ko", value = "더글러스 노엘 애덤스")
        ),
        "sw" to listOf(
            LanguageValuePair(language = "sw", value = "Douglas Noel Adams"),
            LanguageValuePair(language = "sw", value = "Douglas Noël Adams")
        ),
        "tr" to listOf(
            LanguageValuePair(language = "tr", value = "Douglas Noel Adams"),
            LanguageValuePair(language = "tr", value = "Douglas N. Adams"),
            LanguageValuePair(language = "tr", value = "Douglas Noël Adams")
        ),
        "et" to listOf(
            LanguageValuePair(language = "et", value = "Douglas Noël Adams")
        ),
        "ar" to listOf(
            LanguageValuePair(language = "ar", value = "دوغلاس نويل آدمز"),
            LanguageValuePair(language = "ar", value = "دوغلاس ن. آدمز"),
            LanguageValuePair(language = "ar", value = "دوغلاس آدامز"),
            LanguageValuePair(language = "ar", value = "دوجلاس آدمز"),
            LanguageValuePair(language = "ar", value = "دوجلاس آدامز")
        ),
        "la" to listOf(
            LanguageValuePair(language = "la", value = "Duglassius Noëlus Adams"),
            LanguageValuePair(language = "la", value = "Douglas Adams"),
            LanguageValuePair(language = "la", value = "Duglassius Natalis Adams")
        ),
        "gl" to listOf(
            LanguageValuePair(language = "gl", value = "Douglas Noël Adams")
        ),
        "bho" to listOf(
            LanguageValuePair(language = "bho", value = "डग्लस अडम्स"),
            LanguageValuePair(language = "bho", value = "डग्लस एडम्स")
        ),
        "sv" to listOf(
            LanguageValuePair(language = "sv", value = "Douglas Noel Adams"),
            LanguageValuePair(language = "sv", value = "Douglas Noël Adams")
        ),
        "ml" to listOf(
            LanguageValuePair(language = "ml", value = "ഡഗ്ലസ് നോയൽ ആഡംസ്"),
            LanguageValuePair(language = "ml", value = "ഡഗ്ലസ് എന്‍ ആഡംസ്")
        ),
        "nn" to listOf(
            LanguageValuePair(language = "nn", value = "Douglas Noel Adams")
        ),
        "ga" to listOf(
            LanguageValuePair(language = "ga", value = "Douglas Noel Adams"),
            LanguageValuePair(language = "ga", value = "Douglas Noël Adams"),
            LanguageValuePair(language = "ga", value = "Douglas N. Adams")
        ),
        "zh-tw" to listOf(
            LanguageValuePair(language = "zh-tw", value = "道格拉斯·諾耶爾·亞當斯")
        ),
        "ro" to listOf(
            LanguageValuePair(language = "ro", value = "Douglas Noël Adams")
        ),
        "eo" to listOf(
            LanguageValuePair(language = "eo", value = "Douglas ADAMS"),
            LanguageValuePair(language = "eo", value = "Douglas Noël ADAMS")
        ),
        "ca" to listOf(
            LanguageValuePair(language = "ca", value = "Douglas Noel Adams"),
            LanguageValuePair(language = "ca", value = "Douglas Noël Adams")
        ),
        "nqo" to listOf(
            LanguageValuePair(language = "nqo", value = "ߘߎߜ߭ߑߟߊߛ ߣߏߥߍߟ ߊߘߊߡߛ"),
            LanguageValuePair(language = "nqo", value = "ߘߎߜ߭ߑߟߊߛ ߣ. ߊߘߊߡߛ")
        ),
        "pl" to listOf(
            LanguageValuePair(language = "pl", value = "Douglas Noel Adams")
        ),
        "ms" to listOf(
            LanguageValuePair(language = "ms", value = "Douglas Noel Adams"),
            LanguageValuePair(language = "ms", value = "Douglas Noël Adams"),
            LanguageValuePair(language = "ms", value = "Douglas N. Adams")
        ),
        "da" to listOf(
            LanguageValuePair(language = "da", value = "Douglas Noel Adams")
        ),
        "fi" to listOf(
            LanguageValuePair(language = "fi", value = "Douglas Noel Adams")
        ),
        "az" to listOf(
            LanguageValuePair(language = "az", value = "Duqlas Adams"),
            LanguageValuePair(language = "az", value = "Douglas Adams")
        ),
        "mrj" to listOf(
            LanguageValuePair(language = "mrj", value = "Адамс, Дуглас")
        ),
        "ur" to listOf(
            LanguageValuePair(language = "ur", value = "ڈگلس ایڈمس")
        ),
        "lv" to listOf(
            LanguageValuePair(language = "lv", value = "Douglas Adams")
        ),
        "sr-el" to listOf(
            LanguageValuePair(language = "sr-el", value = "Douglas Adams")
        ),
        "ak" to listOf(
            LanguageValuePair(language = "ak", value = "Douglas Adams")
        ),
        "hu" to listOf(
            LanguageValuePair(language = "hu", value = "Douglas Noel Adams")
        ),
        "ia" to listOf(
            LanguageValuePair(language = "ia", value = "Douglas Noel Adams")
        )
    )
)
