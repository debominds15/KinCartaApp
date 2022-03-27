package com.kincarta.app.data.repository

import com.kincarta.app.domain.model.CaseStudies
import com.kincarta.app.domain.model.Sections

object FakeCaseStudiesData {

    val caseStudies = arrayListOf(
        CaseStudies(
            1,
            "TfL",
            "Testing Tube brakes, with TfL Decelerator",
            "Public Sector",
            true,
            "A World-First For Apple iPad",
            "https://raw.githubusercontent.com/theappbusiness/engineering-challenge/main/endpoints/v1/images/decelerator_header-image-2x.jpg",
            arrayListOf(
                Sections(null, listOf()),
                Sections("Reimagining brake testing", listOf()),
                Sections("Inspiring trust through design", listOf())
            ),
            "https://itunes.apple.com/gb/app/the-telegraph-news/id303301873?mt=8"
        ),
        CaseStudies(
            2,
            "Rail Delivery Group",
            "Taking national Railcards from wallet to smartphone",
            "Transport",
            false,
            "From Wallet to Smartphoned",
            "https://raw.githubusercontent.com/theappbusiness/engineering-challenge/main/endpoints/v1/images/rdg_hero_image_1400x520-x2.png",
            arrayListOf(
                Sections(null, listOf()),
                Sections("Setting direction and tempo", listOf()),
                Sections("Mapping real-world painpoints", listOf())
            ),
            "https://itunes.apple.com/us/app/railcard/id1246748048?mt=8"
        )
    )

}