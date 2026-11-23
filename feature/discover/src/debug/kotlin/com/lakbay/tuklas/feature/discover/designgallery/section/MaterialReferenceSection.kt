package com.lakbay.tuklas.feature.discover.designgallery.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.lakbay.tuklas.feature.discover.designgallery.GallerySection
import com.lakbay.tuklas.core.designsystem.theme.TuklasSpacing

@Composable
internal fun MaterialReferenceSection() = GallerySection("Direct Material 3 references") {
    Text("These are standard Material controls, not TuklasTayo shared components.")
    var checked by rememberSaveable { mutableStateOf(false) }
    var switched by rememberSaveable { mutableStateOf(true) }
    var radio by rememberSaveable { mutableStateOf(0) }
    Row(Modifier.fillMaxWidth().heightIn(min = 48.dp).toggleable(checked, role = Role.Checkbox, onValueChange = { checked = it })
        .padding(TuklasSpacing.small), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TuklasSpacing.small)) {
        Checkbox(checked, onCheckedChange = null)
        Text("Reference checkbox")
    }
    Row(Modifier.fillMaxWidth().heightIn(min = 48.dp).toggleable(switched, role = Role.Switch, onValueChange = { switched = it })
        .padding(TuklasSpacing.small), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TuklasSpacing.small)) {
        Switch(switched, onCheckedChange = null)
        Text("Reference switch")
    }
    Column(Modifier.selectableGroup()) {
        repeat(2) { index ->
            Row(Modifier.fillMaxWidth().heightIn(min = 48.dp).selectable(radio == index, role = Role.RadioButton, onClick = { radio = index })
                .padding(TuklasSpacing.small), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(TuklasSpacing.small)) {
                RadioButton(radio == index, onClick = null)
                Text("Reference choice ${index + 1}")
            }
        }
    }
    Text("Indeterminate progress")
    CircularProgressIndicator()
    Text("Illustrative progress: 65%")
    LinearProgressIndicator(progress = { 0.65f }, modifier = Modifier.fillMaxWidth())
}
