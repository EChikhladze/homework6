package com.example.homework6

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import com.example.homework6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val users = HashSet<User>()
    private var deletedUsersCount: Int = 0
    private var updateBtnClickCounter: Int = 0
    private var oldUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener() {
            if (isInputValid()) {
               addUser()
            }

            Log.d("usersList", users.toString())
        }

        binding.btnRemove.setOnClickListener() {
            if (isInputValid()) {
                removeUser()
            }

            Log.d("usersList", users.toString())
        }

        binding.btnUpdate.setOnClickListener() {
            if (isInputValid()) {
                updateUser()
            }

            Log.d("usersList", users.toString())
        }
    }

    private fun addUser() {
        val user = createUser()

        if (!users.contains(user)) {
            users.add(user)
            outputTVText(getString(R.string.add_success_msg), Color.GREEN)
        } else {
            outputTVText(getString(R.string.add_error_msg), Color.RED)
        }

        clearFields()
        updateCountTVs()
    }

    private fun removeUser() {
        val user = createUser()

        if (users.contains(user)) {
            users.remove(user)
            outputTVText(getString(R.string.remove_success_msg), Color.GREEN)
            deletedUsersCount++
        } else {
            outputTVText(getString(R.string.remove_error_msg), Color.RED)
        }

        clearFields()
        updateCountTVs()
    }

    private fun updateUser() {
        updateBtnClickCounter++

        if (updateBtnClickCounter == 2) {
            val updatedUser = createUser()

            users.remove(oldUser)
            users.add(updatedUser)

            outputTVText(getString(R.string.update_success_msg), Color.GREEN)
            updateBtnClickCounter = 0
        } else {
            oldUser = createUser()
            outputTVText(getString(R.string.update_prompt), Color.GRAY)
            if (!users.contains(oldUser)) {
                oldUser = null
                outputTVText(getString(R.string.remove_error_msg), Color.RED)
                updateBtnClickCounter = 0
            }
        }

        clearFields()
    }

    private fun createUser(): User {
        return User(binding.edFirstName.text.toString(),
                    binding. edLastName.text.toString(),
                    binding.edAge.text.toString().toInt(),
                    binding.edEmail.text.toString())
    }

    private fun updateCountTVs() {
        binding.tvActiveUsersNum.text = users.size.toString()
        binding.tvDeletedUsersNum.text = deletedUsersCount.toString()
    }

    private fun outputTVText(text: String, color: Int) {
        val whiteTint = ColorStateList.valueOf(Color.WHITE)
        binding.tvOperationState.backgroundTintList = whiteTint
        binding.tvOperationState.setTextColor(color)
        binding.tvOperationState.text = text
    }

    private fun isInputValid(): Boolean {
        isNotEmpty()
        isAgePositiveInt(binding.edAge)
        isEmailValid(binding.edEmail)
        if (binding.edEmail.text.toString().isNotEmpty()
            && binding.edFirstName.text.toString().isNotEmpty()
            && binding.edLastName.text.toString().isNotEmpty()
            && binding.edAge.text.toString().isNotEmpty()) return true
        return false
    }

    private fun isNotEmpty() {
        val fieldsList = listOf(binding.edFirstName, binding.edLastName, binding.edAge, binding.edEmail)

        for (element in fieldsList) {
            if (element.text.toString().isEmpty()) {
                displayFieldErrorMessage(element, getString(R.string.requiredField))
            }
        }
    }

    private fun isEmailValid(email: AppCompatEditText) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            displayFieldErrorMessage(email, getString(R.string.invalid_input))
        }
    }

    private fun isAgePositiveInt(age: AppCompatEditText) {
        if (!Regex("^[0-9]+$").matches(age.text.toString())) {
            displayFieldErrorMessage(age, getString(R.string.invalid_input))
        }
    }

    private fun displayFieldErrorMessage(element: AppCompatEditText, errorMsg: String) {
        element.setText("")
        element.setHintTextColor(Color.RED)
        element.hint = errorMsg
    }

    private fun clearFields() {
        binding.edFirstName.setText("")
        binding.edFirstName.setHintTextColor(Color.GRAY)
        binding.edFirstName.hint = getString(R.string.first_name)
        binding.edLastName.setText("")
        binding.edLastName.setHintTextColor(Color.GRAY)
        binding.edLastName.hint = getString(R.string.last_name)
        binding.edAge.setText("")
        binding.edAge.setHintTextColor(Color.GRAY)
        binding.edAge.hint = getString(R.string.age)
        binding.edEmail.setText("")
        binding.edEmail.setHintTextColor(Color.GRAY)
        binding.edEmail.hint = getString(R.string.email)
    }

    data class User(var firstName: String, var lastName: String, var age: Int, var email: String)
}