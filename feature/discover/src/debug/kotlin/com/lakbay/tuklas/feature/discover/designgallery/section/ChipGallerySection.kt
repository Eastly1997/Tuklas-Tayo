package com.lakbay.tuklas.feature.discover.designgallery.section

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.lakbay.tuklas.feature.discover.designgallery.GallerySection
import com.lakbay.tuklas.core.designsystem.component.TuklasChip
import com.lakbay.tuklas.core.designsystem.component.TuklasChipType

@Composable
internal fun ChipGallerySection(onAction: (String) -> Unit) = GallerySection("Chips") {
    TuklasChip(onClick = { onAction("Assist clicked") }, label = { Text("Assist") })
    TuklasChip(onClick = {}, label = { Text("Assist disabled") }, enabled = false)
    TuklasChip(onClick = { onAction("Suggestion clicked") }, label = { Text("Suggestion") },
        type = TuklasChipType.Suggestion)
    TuklasChip(onClick = {}, label = { Text("Suggestion disabled") },
        type = TuklasChipType.Suggestion, enabled = false)
    var filterSelected by rememberSaveable { mutableStateOf(true) }
    var otherFilterSelected by rememberSaveable { mutableStateOf(false) }
    var inputSelected by rememberSaveable { mutableStateOf(true) }
    var otherInputSelected by rememberSaveable { mutableStateOf(false) }
    TuklasChip(onClick = { filterSelected = !filterSelected }, label = { Text("Filter: $filterSelected") },
        type = TuklasChipType.Filter(filterSelected))
    TuklasChip(onClick = { otherFilterSelected = !otherFilterSelected }, label = { Text("Filter: $otherFilterSelected") },
        type = TuklasChipType.Filter(otherFilterSelected))
    TuklasChip(onClick = { inputSelected = !inputSelected }, label = { Text("Input: $inputSelected") },
        type = TuklasChipType.Input(inputSelected))
    TuklasChip(onClick = { otherInputSelected = !otherInputSelected }, label = { Text("Input: $otherInputSelected") },
        type = TuklasChipType.Input(otherInputSelected))
    listOf(false, true).forEach { selected ->
        TuklasChip(onClick = {}, label = { Text("Filter disabled: $selected") },
            type = TuklasChipType.Filter(selected), enabled = false)
        TuklasChip(onClick = {}, label = { Text("Input disabled: $selected") },
            type = TuklasChipType.Input(selected), enabled = false)
    }
}
