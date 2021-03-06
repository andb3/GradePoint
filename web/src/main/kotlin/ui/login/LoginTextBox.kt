package ui.login

import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import react.RBuilder
import styled.css
import styled.styledInput
import ui.dp
import ui.sp
import util.targetInputValue

fun RBuilder.LoginTextBox(placeholder: String, onChange: (String) -> Unit, password: Boolean = false, cssBuilder: CSSBuilder.() -> Unit){
    styledInput(
        type = if (password) InputType.password else InputType.text
    ) {
        css {
            padding(all = 16.dp)
            fontWeight = FontWeight.w600
            fontSize = 36.sp
            borderWidth = 0.dp
            backgroundColor = rgba(0, 0, 0, 0.05)
            "::placeholder"{
                color = rgba(0, 0, 0, 0.2)
            }
            width = 100.pct
            boxSizing = BoxSizing.borderBox
            cssBuilder.invoke(this)
        }
        attrs {
            this.placeholder = placeholder
            this.onChangeFunction = {
                val value = it.targetInputValue
                onChange.invoke(value)
            }
        }
    }
}