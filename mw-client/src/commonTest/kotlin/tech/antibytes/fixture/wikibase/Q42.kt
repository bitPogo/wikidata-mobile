/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.fixture.wikibase

import kotlinx.datetime.Instant
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.wikibase.model.Alias
import tech.antibytes.mediawiki.wikibase.model.Description
import tech.antibytes.mediawiki.wikibase.model.Entity
import tech.antibytes.mediawiki.wikibase.model.Label

internal val q42 = Entity(
    id = "Q42",
    revisionId = 1558961212,
    lastModification = Instant.parse("2022-01-09T05:52:11Z"),
    type = DataModelContract.EntityTypes.ITEM,
    labels = mapOf(
        "en" to Label(language = "en", value = "Douglas Adams"),
        "fr" to Label(language = "fr", value = "Douglas Adams"),
        "ru" to Label(language = "ru", value = "Дуглас Адамс"),
        "pl" to Label(language = "pl", value = "Douglas Adams"),
        "it" to Label(language = "it", value = "Douglas Adams"),
        "en-gb" to Label(language = "en-gb", value = "Douglas Adams"),
        "nb" to Label(language = "nb", value = "Douglas Adams"),
        "es" to Label(language = "es", value = "Douglas Adams"),
        "en-ca" to Label(language = "en-ca", value = "Douglas Adams"),
        "hr" to Label(language = "hr", value = "Douglas Adams"),
        "pt" to Label(language = "pt", value = "Douglas Adams"),
        "ko" to Label(language = "ko", value = "더글러스 애덤스"),
        "nl" to Label(language = "nl", value = "Douglas Adams"),
        "el" to Label(language = "el", value = "Ντάγκλας Άνταμς"),
        "ar" to Label(language = "ar", value = "دوغلاس آدمز"),
        "arz" to Label(language = "arz", value = "دوجلاس ادامز"),
        "bar" to Label(language = "bar", value = "Douglas Adams"),
        "be" to Label(language = "be", value = "Дуглас Адамс"),
        "bg" to Label(language = "bg", value = "Дъглас Адамс"),
        "bs" to Label(language = "bs", value = "Douglas Adams"),
        "ca" to Label(language = "ca", value = "Douglas Adams"),
        "cs" to Label(language = "cs", value = "Douglas Adams"),
        "cy" to Label(language = "cy", value = "Douglas Adams"),
        "da" to Label(language = "da", value = "Douglas Adams"),
        "eo" to Label(language = "eo", value = "Douglas Adams"),
        "et" to Label(language = "et", value = "Douglas Adams"),
        "fa" to Label(language = "fa", value = "داگلاس آدامز"),
        "fi" to Label(language = "fi", value = "Douglas Adams"),
        "ga" to Label(language = "ga", value = "Douglas Adams"),
        "gl" to Label(language = "gl", value = "Douglas Adams"),
        "he" to Label(language = "he", value = "דאגלס אדמס"),
        "hu" to Label(language = "hu", value = "Douglas Adams"),
        "id" to Label(language = "id", value = "Douglas Adams"),
        "io" to Label(language = "io", value = "Douglas Adams"),
        "is" to Label(language = "is", value = "Douglas Adams"),
        "ja" to Label(language = "ja", value = "ダグラス・アダムズ"),
        "jv" to Label(language = "jv", value = "Douglas Adams"),
        "ka" to Label(language = "ka", value = "დაგლას ადამსი"),
        "la" to Label(language = "la", value = "Duglassius Adams"),
        "lv" to Label(language = "lv", value = "Duglass Adamss"),
        "mk" to Label(language = "mk", value = "Даглас Адамс"),
        "mr" to Label(language = "mr", value = "डग्लस अॅडम्स"),
        "nn" to Label(language = "nn", value = "Douglas Adams"),
        "ro" to Label(language = "ro", value = "Douglas Adams"),
        "sco" to Label(language = "sco", value = "Douglas Adams"),
        "sh" to Label(language = "sh", value = "Douglas Adams"),
        "sk" to Label(language = "sk", value = "Douglas Adams"),
        "sl" to Label(language = "sl", value = "Douglas Adams"),
        "sq" to Label(language = "sq", value = "Douglas Adams"),
        "sr" to Label(language = "sr", value = "Даглас Адамс"),
        "sv" to Label(language = "sv", value = "Douglas Adams"),
        "ta" to Label(language = "ta", value = "டக்ளஸ் ஆடம்ஸ்"),
        "tr" to Label(language = "tr", value = "Douglas Adams"),
        "uk" to Label(language = "uk", value = "Дуглас Адамс"),
        "vi" to Label(language = "vi", value = "Douglas Adams"),
        "zh" to Label(language = "zh", value = "道格拉斯·亞當斯"),
        "zh-cn" to Label(language = "zh-cn", value = "道格拉斯·亚当斯"),
        "zh-hans" to Label(language = "zh-hans", value = "道格拉斯·亚当斯"),
        "zh-hant" to Label(language = "zh-hant", value = "道格拉斯·亞當斯"),
        "de-ch" to Label(language = "de-ch", value = "Douglas Adams"),
        "pt-br" to Label(language = "pt-br", value = "Douglas Adams"),
        "zh-sg" to Label(language = "zh-sg", value = "道格拉斯·亚当斯"),
        "zh-my" to Label(language = "zh-my", value = "道格拉斯·亚当斯"),
        "zh-hk" to Label(language = "zh-hk", value = "道格拉斯·亞當斯"),
        "zh-tw" to Label(language = "zh-tw", value = "道格拉斯·亞當斯"),
        "zh-mo" to Label(language = "zh-mo", value = "道格拉斯·亞當斯"),
        "war" to Label(language = "war", value = "Douglas Adams"),
        "be-tarask" to Label(language = "be-tarask", value = "Дуглас Адамз"),
        "vep" to Label(language = "vep", value = "Adams Duglas"),
        "ur" to Label(language = "ur", value = "ڈگلس ایڈم"),
        "oc" to Label(language = "oc", value = "Douglas Adams"),
        "af" to Label(language = "af", value = "Douglas Adams"),
        "an" to Label(language = "an", value = "Douglas Adams"),
        "br" to Label(language = "br", value = "Douglas Adams"),
        "eu" to Label(language = "eu", value = "Douglas Adams"),
        "lb" to Label(language = "lb", value = "Douglas Adams"),
        "lmo" to Label(language = "lmo", value = "Douglas Adams"),
        "lt" to Label(language = "lt", value = "Douglas Adams"),
        "nds" to Label(language = "nds", value = "Douglas Adams"),
        "nds-nl" to Label(language = "nds-nl", value = "Douglas Adams"),
        "pms" to Label(language = "pms", value = "Douglas Adams"),
        "vec" to Label(language = "vec", value = "Douglas Adams"),
        "wa" to Label(language = "wa", value = "Douglas Adams"),
        "sr-ec" to Label(language = "sr-ec", value = "Даглас Адамс"),
        "sr-el" to Label(language = "sr-el", value = "Daglas Adams"),
        "de" to Label(language = "de", value = "Douglas Adams"),
        "ckb" to Label(language = "ckb", value = "دەگلاس ئادمز"),
        "fo" to Label(language = "fo", value = "Douglas Adams"),
        "kl" to Label(language = "kl", value = "Douglas Adams"),
        "gsw" to Label(language = "gsw", value = "Douglas Adams"),
        "te" to Label(language = "te", value = "డగ్లస్ ఆడమ్స్"),
        "si" to Label(language = "si", value = "ඩග්ලස් ඇඩම්ස්"),
        "bn" to Label(language = "bn", value = "ডগলাস অ্যাডামস"),
        "hi" to Label(language = "hi", value = "डग्लस अ‍डम्स"),
        "rwr" to Label(language = "rwr", value = "डग्लस अ‍डम्स"),
        "mg" to Label(language = "mg", value = "Douglas Adams"),
        "ml" to Label(language = "ml", value = "ഡഗ്ലസ് ആഡംസ്"),
        "gu" to Label(language = "gu", value = "ડગ્લાસ એડમ્સ"),
        "hy" to Label(language = "hy", value = "Դուգլաս Ադամս"),
        "ast" to Label(language = "ast", value = "Douglas Adams"),
        "co" to Label(language = "co", value = "Douglas Adams"),
        "de-at" to Label(language = "de-at", value = "Douglas Adams"),
        "frp" to Label(language = "frp", value = "Douglas Adams"),
        "fur" to Label(language = "fur", value = "Douglas Adams"),
        "gd" to Label(language = "gd", value = "Douglas Adams"),
        "ia" to Label(language = "ia", value = "Douglas Adams"),
        "ie" to Label(language = "ie", value = "Douglas Adams"),
        "kg" to Label(language = "kg", value = "Douglas Adams"),
        "li" to Label(language = "li", value = "Douglas Adams"),
        "lij" to Label(language = "lij", value = "Douglas Adams"),
        "min" to Label(language = "min", value = "Douglas Adams"),
        "ms" to Label(language = "ms", value = "Douglas Adams"),
        "nap" to Label(language = "nap", value = "Douglas Adams"),
        "nrm" to Label(language = "nrm", value = "Douglas Adams"),
        "pcd" to Label(language = "pcd", value = "Douglas Adams"),
        "rm" to Label(language = "rm", value = "Douglas Adams"),
        "sc" to Label(language = "sc", value = "Douglas Adams"),
        "scn" to Label(language = "scn", value = "Douglas Adams"),
        "sw" to Label(language = "sw", value = "Douglas Adams"),
        "vls" to Label(language = "vls", value = "Douglas Adams"),
        "vo" to Label(language = "vo", value = "Douglas Adams"),
        "wo" to Label(language = "wo", value = "Douglas Adams"),
        "zu" to Label(language = "zu", value = "Douglas Adams"),
        "az" to Label(language = "az", value = "Duqlas Noel Adams"),
        "ak" to Label(language = "ak", value = "Doglas Adams"),
        "or" to Label(language = "or", value = "ଡଗ୍\u200Cଲାସ୍\u200C ଆଦାମ୍\u200Cସ"),
        "kn" to Label(language = "kn", value = "ಡಗ್ಲಸ್ ಆಡಮ್ಸ್"),
        "ne" to Label(language = "ne", value = "डगलस एडम्स"),
        "mrj" to Label(language = "mrj", value = "Адамс"),
        "th" to Label(language = "th", value = "ดักลัส แอดัมส์"),
        "pa" to Label(language = "pa", value = "ਡਗਲਸ ਐਡਮਜ਼"),
        "tcy" to Label(language = "tcy", value = "ಡಾಗ್ಲಸ್ ಆಡಮ್ಸ್"),
        "tl" to Label(language = "tl", value = "Douglas Adams"),
        "ext" to Label(language = "ext", value = "Douglas Adams"),
        "azb" to Label(language = "azb", value = "داقلاس آدامز"),
        "lfn" to Label(language = "lfn", value = "Douglas Adams"),
        "nan" to Label(language = "nan", value = "Douglas Adams"),
        "ky" to Label(language = "ky", value = "Дуглас Адамс"),
        "bho" to Label(language = "bho", value = "डगलस एडम्स"),
        "wuu" to Label(language = "wuu", value = "道格拉斯·亚当斯"),
        "yue" to Label(language = "yue", value = "道格拉斯亞當斯"),
        "pnb" to Label(language = "pnb", value = "ڈگلس ایڈمس"),
        "sje" to Label(language = "sje", value = "Douglas Adams"),
        "se" to Label(language = "se", value = "Douglas Adams"),
        "smn" to Label(language = "smn", value = "Douglas Adams"),
        "sms" to Label(language = "sms", value = "Douglas Adams"),
        "sma" to Label(language = "sma", value = "Douglas Adams"),
        "smj" to Label(language = "smj", value = "Douglas Adams"),
        "bm" to Label(language = "bm", value = "Douglas Adams"),
        "frc" to Label(language = "frc", value = "Douglas Adams"),
        "jam" to Label(language = "jam", value = "Douglas Adams"),
        "kab" to Label(language = "kab", value = "Douglas Adams"),
        "pap" to Label(language = "pap", value = "Douglas Adams"),
        "prg" to Label(language = "prg", value = "Douglas Adams"),
        "rgn" to Label(language = "rgn", value = "Douglas Adams"),
        "vmf" to Label(language = "vmf", value = "Douglas Adams"),
        "nqo" to Label(language = "nqo", value = "ߘߎߜ߭ߑߟߊߛ ߊߘߊߡߛ"),
        "fy" to Label(language = "fy", value = "Douglas Adams"),
        "hsb" to Label(language = "hsb", value = "Douglas Adams"),
        "yi" to Label(language = "yi", value = "דאַגלאַס אַדאַמס"),
        "kw" to Label(language = "kw", value = "Douglas Adams"),
        "ms-arab" to Label(language = "ms-arab", value = "دݢلس ايدمﺯ"),
        "jut" to Label(language = "jut", value = "Douglas Adams")
    ),
    descriptions = mapOf(
        "en" to Description(language = "en", value = "English writer and humorist (1952-2001)"),
        "fr" to Description(language = "fr", value = "écrivain anglais de science-fiction"),
        "en-gb" to Description(language = "en-gb", value = "English writer and humourist"),
        "nb" to Description(language = "nb", value = "engelsk science fiction-forfatter og humorist"),
        "it" to Description(language = "it", value = "scrittore ed umorista britannico"),
        "de" to Description(language = "de", value = "britischer Schriftsteller"),
        "es" to Description(language = "es", value = "escritor y humorista británico"),
        "ru" to Description(language = "ru", value = "английский писатель, драматург и сценарист, автор серии книг «Автостопом по галактике»"),
        "zh-hans" to Description(language = "zh-hans", value = "英国作家"),
        "zh-hant" to Description(language = "zh-hant", value = "英國作家"),
        "zh-cn" to Description(language = "zh-cn", value = "英国作家"),
        "zh-sg" to Description(language = "zh-sg", value = "英国作家"),
        "zh-my" to Description(language = "zh-my", value = "英国作家"),
        "zh" to Description(language = "zh", value = "英國作家"),
        "zh-hk" to Description(language = "zh-hk", value = "英國作家"),
        "zh-tw" to Description(language = "zh-tw", value = "英國文學家、幽默作家"),
        "zh-mo" to Description(language = "zh-mo", value = "英國作家"),
        "ca" to Description(language = "ca", value = "escriptor anglès"),
        "fi" to Description(language = "fi", value = "englantilainen kirjailija ja humoristi"),
        "cs" to Description(language = "cs", value = "anglický spisovatel, humorista a dramatik"),
        "sv" to Description(language = "sv", value = "brittisk författare (1952–2001)"),
        "pt-br" to Description(language = "pt-br", value = "escritor e comediante britânico"),
        "ta" to Description(language = "ta", value = "ஆங்கில எழுத்தாளர் மற்றும் நகைச்சுவையாளர்"),
        "sl" to Description(language = "sl", value = "angleški pisatelj, humorist in dramatik"),
        "da" to Description(language = "da", value = "engelsk forfatter"),
        "nl" to Description(language = "nl", value = "Engelse schrijver (1952-2001)"),
        "pt" to Description(language = "pt", value = "escritor e comediante britânico"),
        "pl" to Description(language = "pl", value = "brytyjski pisarz"),
        "lv" to Description(language = "lv", value = "angļu zinātniskās fantastikas rakstnieks un humorists"),
        "sr" to Description(language = "sr", value = "енглески писац и хумориста"),
        "sr-ec" to Description(language = "sr-ec", value = "енглески писац научне фантастике и хумориста"),
        "sr-el" to Description(language = "sr-el", value = "engleski pisac naučne fantastike i humorista"),
        "eo" to Description(language = "eo", value = "angla aŭtoro de sciencfikcio-romanoj kaj humoristo"),
        "bar" to Description(language = "bar", value = "a englischer Science-Fiction-Schriftsteller"),
        "br" to Description(language = "br", value = "skrivagner saoznek"),
        "ja" to Description(language = "ja", value = "イングランドの作家"),
        "nn" to Description(language = "nn", value = "forfattar"),
        "tr" to Description(language = "tr", value = "İngiliz yazar"),
        "si" to Description(language = "si", value = "ඉංග්‍රීසි කවියෙක්"),
        "vi" to Description(language = "vi", value = "Nhà văn và nhà soạn hài kịch người Anh"),
        "cy" to Description(language = "cy", value = "awdur a dychanwr Seisnig"),
        "gu" to Description(language = "gu", value = "અંગ્રેજી લેખક અને હાસ્યકાર"),
        "uk" to Description(language = "uk", value = "британський комічний радіодраматург, письменник"),
        "ro" to Description(language = "ro", value = "scriitor, dramaturg englez"),
        "hu" to Description(language = "hu", value = "angol író"),
        "fa" to Description(language = "fa", value = "فیلمنامه‌نویس و نویسنده بریتانیایی"),
        "af" to Description(language = "af", value = "Engelse skrywer en humoris"),
        "mk" to Description(language = "mk", value = "англиски писател и хуморист"),
        "el" to Description(language = "el", value = "Άγγλος συγγραφέας"),
        "hy" to Description(language = "hy", value = "անգլիացի գրող, դրամատուրգ, սցենարիստ, «Ավտոստոպով զբոսաշրջիկի միջգալակտիկական ուղեցույց» վեպերի շարք"),
        "bg" to Description(language = "bg", value = "английски писател и хуморист"),
        "ne" to Description(language = "ne", value = "अङ्ग्रेजी लेखक र व्यङ्ग्यकार"),
        "he" to Description(language = "he", value = "סופר והומוריסטן בריטי"),
        "de-at" to Description(language = "de-at", value = "britischer Schriftsteller"),
        "de-ch" to Description(language = "de-ch", value = "britischer Schriftsteller"),
        "gsw" to Description(language = "gsw", value = "britischer Schriftsteller"),
        "nds" to Description(language = "nds", value = "englischer Schriftsteller"),
        "kn" to Description(language = "kn", value = "ಇಂಗ್ಲಿಷ್ ಭಾಷೆಯ ಬರಹಗಾರ ಹಾಗೂ ಹಾಸ್ಯ ಲೇಖಕ"),
        "pa" to Description(language = "pa", value = "ਅੰਗਰੇਜ਼ੀ ਲੇਖਕ"),
        "ar" to Description(language = "ar", value = "كاتب إنجليزي فكاهي"),
        "tl" to Description(language = "tl", value = "taga-Inglatera na manunulat at tagapagpatawa"),
        "eu" to Description(language = "eu", value = "idazle eta umorista britaniarra"),
        "hr" to Description(language = "hr", value = "britanski radijski dramaturg i pisac znanstvene fantastike"),
        "ko" to Description(language = "ko", value = "영국의 작가"),
        "sw" to Description(language = "sw", value = "mwandishi Mwingereza"),
        "th" to Description(language = "th", value = "นักเขียนและผู้เล่าเรื่องอารมณ์ขันชาวอังกฤษ"),
        "en-ca" to Description(language = "en-ca", value = "English writer"),
        "gd" to Description(language = "gd", value = "sgrìobhadair Sasannach"),
        "ka" to Description(language = "ka", value = "ინგლისელი მწერალი და იუმორისტი"),
        "et" to Description(language = "et", value = "inglise ulmekirjanik"),
        "te" to Description(language = "te", value = "ఇంగ్లీషు రచయిత, హాస్యకారుడు"),
        "ast" to Description(language = "ast", value = "escritor y humorista inglés"),
        "sq" to Description(language = "sq", value = "autor dhe humorist anglez"),
        "gl" to Description(language = "gl", value = "escritor e guionista británico"),
        "bho" to Description(language = "bho", value = "अंग्रेजी भाषा के ब्रिटिश लेखक"),
        "sk" to Description(language = "sk", value = "anglický spisovateľ"),
        "la" to Description(language = "la", value = "scriptor, scriptor scaenicus, et mythistoricus (1952–2001)"),
        "ml" to Description(language = "ml", value = "ബ്രിട്ടീഷ് എഴുത്തുകാരനും ഹാസ്യസാഹിത്യാകാരനും"),
        "ga" to Description(language = "ga", value = "scríbhneoir Sasanach"),
        "ku-latn" to Description(language = "ku-latn", value = "nivîskarê brîtanî"),
        "min" to Description(language = "min", value = "Panulih jo palawak dari Inggirih"),
        "hi" to Description(language = "hi", value = "अंग्रेजी लेखक"),
        "nqo" to Description(language = "nqo", value = "ߊ߲߬ߜ߭ߌ߬ߟߋ߬ ߛߓߍߦߟߊ ߞߎ߲߬ߘߐ߬ߕߍ߰ ߟߐ߲ߞߏ ߞߊ߲߬"),
        "be" to Description(language = "be", value = "англійскі пісьменнік"),
        "id" to Description(language = "id", value = "penulis Britania Raya"),
        "bs" to Description(language = "bs", value = "engleski pisac i humorist"),
        "ms" to Description(language = "ms", value = "penulis dan pelawak Inggeris"),
        "mr" to Description(language = "mr", value = "ब्रिटिश लेखक व नाटककार"),
        "bn" to Description(language = "bn", value = "ইংরেজ লেখক ও কৌতুকবিদ"),
        "dv" to Description(language = "dv", value = "ލިޔުންތެރިއެއް")
    ),
    aliases = mapOf(
        "en" to listOf(
            Alias(language = "en", value = "Douglas Noel Adams"),
            Alias(language = "en", value = "Douglas Noël Adams"),
            Alias(language = "en", value = "Douglas N. Adams")
        ),
        "ru" to listOf(
            Alias(language = "ru", value = "Адамс, Дуглас"),
            Alias(language = "ru", value = "Дуглас Ноэль Адамс"),
            Alias(language = "ru", value = "Адамс, Дуглас Ноэль")
        ),
        "nb" to listOf(
            Alias(language = "nb", value = "Douglas Noël Adams"),
            Alias(language = "nb", value = "Douglas N. Adams")
        ),
        "fr" to listOf(
            Alias(language = "fr", value = "Douglas Noel Adams"),
            Alias(language = "fr", value = "Douglas Noël Adams")
        ),
        "de" to listOf(
            Alias(language = "de", value = "Douglas Noel Adams"),
            Alias(language = "de", value = "Douglas Noël Adams")
        ),
        "pt-br" to listOf(
            Alias(language = "pt-br", value = "Douglas Noël Adams"),
            Alias(language = "pt-br", value = "Douglas Noel Adams")
        ),
        "be-tarask" to listOf(
            Alias(language = "be-tarask", value = "Дуглас Адамс")
        ),
        "zh" to listOf(
            Alias(language = "zh", value = "亞當斯")
        ),
        "es" to listOf(
            Alias(language = "es", value = "Douglas Noel Adams"),
            Alias(language = "es", value = "Douglas Noël Adams")
        ),
        "it" to listOf(
            Alias(language = "it", value = "Douglas Noel Adams"),
            Alias(language = "it", value = "Douglas N. Adams")
        ),
        "cs" to listOf(
            Alias(language = "cs", value = "Douglas Noël Adams"),
            Alias(language = "cs", value = "Douglas Noel Adams"),
            Alias(language = "cs", value = "Douglas N. Adams")
        ),
        "hy" to listOf(
            Alias(language = "hy", value = "Ադամս, Դուգլաս")
        ),
        "el" to listOf(
            Alias(language = "el", value = "Ντάγκλας Νόελ Άνταμς")
        ),
        "nl" to listOf(
            Alias(language = "nl", value = "Douglas Noel Adams"),
            Alias(language = "nl", value = "Douglas Noël Adams")
        ),
        "pt" to listOf(
            Alias(language = "pt", value = "Douglas Noël Adams"),
            Alias(language = "pt", value = "Douglas Noel Adams")
        ),
        "ja" to listOf(
            Alias(language = "ja", value = "ダグラス・アダムス")
        ),
        "pa" to listOf(
            Alias(language = "pa", value = "ਡਗਲਸ ਨੋਏਲ ਐਡਮਜ਼"),
            Alias(language = "pa", value = "ਡਗਲਸ ਐਡਮਸ")
        ),
        "tl" to listOf(
            Alias(language = "tl", value = "Douglas Noël Adams"),
            Alias(language = "tl", value = "Douglas Noel Adams")
        ),
        "eu" to listOf(
            Alias(language = "eu", value = "Douglas Noel Adams"),
            Alias(language = "eu", value = "Douglas Noël Adams")
        ),
        "uk" to listOf(
            Alias(language = "uk", value = "Дуглас Ноел Адамс"),
            Alias(language = "uk", value = "Адамс Дуглас")
        ),
        "hr" to listOf(
            Alias(language = "hr", value = "Douglas Noël Adams"),
            Alias(language = "hr", value = "Douglas N. Adams"),
            Alias(language = "hr", value = "Douglas Noel Adams")
        ),
        "he" to listOf(
            Alias(language = "he", value = "דגלס אדמס"),
            Alias(language = "he", value = "דאגלס נואל אדמס")
        ),
        "ko" to listOf(
            Alias(language = "ko", value = "더글라스 애덤스"),
            Alias(language = "ko", value = "더글러스 노엘 애덤스")
        ),
        "sw" to listOf(
            Alias(language = "sw", value = "Douglas Noel Adams"),
            Alias(language = "sw", value = "Douglas Noël Adams")
        ),
        "tr" to listOf(
            Alias(language = "tr", value = "Douglas Noel Adams"),
            Alias(language = "tr", value = "Douglas N. Adams"),
            Alias(language = "tr", value = "Douglas Noël Adams")
        ),
        "et" to listOf(
            Alias(language = "et", value = "Douglas Noël Adams")
        ),
        "ar" to listOf(
            Alias(language = "ar", value = "دوغلاس نويل آدمز"),
            Alias(language = "ar", value = "دوغلاس ن. آدمز"),
            Alias(language = "ar", value = "دوغلاس آدامز"),
            Alias(language = "ar", value = "دوجلاس آدمز"),
            Alias(language = "ar", value = "دوجلاس آدامز")
        ),
        "la" to listOf(
            Alias(language = "la", value = "Duglassius Noëlus Adams"),
            Alias(language = "la", value = "Douglas Adams"),
            Alias(language = "la", value = "Duglassius Natalis Adams")
        ),
        "gl" to listOf(
            Alias(language = "gl", value = "Douglas Noël Adams")
        ),
        "bho" to listOf(
            Alias(language = "bho", value = "डग्लस अडम्स"),
            Alias(language = "bho", value = "डग्लस एडम्स")
        ),
        "sv" to listOf(
            Alias(language = "sv", value = "Douglas Noel Adams"),
            Alias(language = "sv", value = "Douglas Noël Adams")
        ),
        "ml" to listOf(
            Alias(language = "ml", value = "ഡഗ്ലസ് നോയൽ ആഡംസ്"),
            Alias(language = "ml", value = "ഡഗ്ലസ് എന്‍ ആഡംസ്")
        ),
        "nn" to listOf(
            Alias(language = "nn", value = "Douglas Noel Adams")
        ),
        "ga" to listOf(
            Alias(language = "ga", value = "Douglas Noel Adams"),
            Alias(language = "ga", value = "Douglas Noël Adams"),
            Alias(language = "ga", value = "Douglas N. Adams")
        ),
        "zh-tw" to listOf(
            Alias(language = "zh-tw", value = "道格拉斯·諾耶爾·亞當斯")
        ),
        "ro" to listOf(
            Alias(language = "ro", value = "Douglas Noël Adams")
        ),
        "eo" to listOf(
            Alias(language = "eo", value = "Douglas ADAMS"),
            Alias(language = "eo", value = "Douglas Noël ADAMS")
        ),
        "ca" to listOf(
            Alias(language = "ca", value = "Douglas Noel Adams"),
            Alias(language = "ca", value = "Douglas Noël Adams")
        ),
        "nqo" to listOf(
            Alias(language = "nqo", value = "ߘߎߜ߭ߑߟߊߛ ߣߏߥߍߟ ߊߘߊߡߛ"),
            Alias(language = "nqo", value = "ߘߎߜ߭ߑߟߊߛ ߣ. ߊߘߊߡߛ")
        ),
        "pl" to listOf(
            Alias(language = "pl", value = "Douglas Noel Adams")
        ),
        "ms" to listOf(
            Alias(language = "ms", value = "Douglas Noel Adams"),
            Alias(language = "ms", value = "Douglas Noël Adams"),
            Alias(language = "ms", value = "Douglas N. Adams")
        ),
        "da" to listOf(
            Alias(language = "da", value = "Douglas Noel Adams")
        ),
        "fi" to listOf(
            Alias(language = "fi", value = "Douglas Noel Adams")
        ),
        "az" to listOf(
            Alias(language = "az", value = "Duqlas Adams"),
            Alias(language = "az", value = "Douglas Adams")
        ),
        "mrj" to listOf(
            Alias(language = "mrj", value = "Адамс, Дуглас")
        ),
        "ur" to listOf(
            Alias(language = "ur", value = "ڈگلس ایڈمس")
        ),
        "lv" to listOf(
            Alias(language = "lv", value = "Douglas Adams")
        ),
        "sr-el" to listOf(
            Alias(language = "sr-el", value = "Douglas Adams")
        ),
        "ak" to listOf(
            Alias(language = "ak", value = "Douglas Adams")
        ),
        "hu" to listOf(
            Alias(language = "hu", value = "Douglas Noel Adams")
        ),
        "ia" to listOf(
            Alias(language = "ia", value = "Douglas Noel Adams")
        )
    )
)
