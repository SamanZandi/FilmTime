package com.zandroid.filimo_mvvm.ui.register

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zandroid.filimo_mvvm.R
import com.zandroid.filimo_mvvm.data.models.register.BodyRegister
import com.zandroid.filimo_mvvm.databinding.FragmentRegisterBinding
import com.zandroid.filimo_mvvm.utils.showSnackBar
import com.zandroid.filimo_mvvm.viewmodel.RegisterViewModel
import com.zandroid.filimo_mvvm.utils.NetworkRequest
import com.zandroid.filimo_mvvm.utils.showVisibility
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    //Binding
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    //Others
    private val viewModel:RegisterViewModel by viewModels()
    private var username=""
    private var userEmail=""
    private var userPassword=""
    private var state="0"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            viewModel.registerData.observe(viewLifecycleOwner){response->
                when(response){
                    is NetworkRequest.Loading->{
                        loading.showVisibility(true,btnSubmit)
                    }

                    is NetworkRequest.Success -> {
                        loading.showVisibility(false,btnSubmit)
                        response.data?.let {
                            state=it.aLLINONEVIDEO?.get(0)?.success!!
                            if(state=="1"){
                                viewModel.saveState(it.aLLINONEVIDEO.get(0)?.msg!!,state,userEmail)
                                view.showSnackBar(
                                    getString(R.string.success_msg),
                                    ContextCompat.getColor(requireContext(),R.color.caribbean_green))
                                findNavController().popBackStack(R.id.registerFragment,true)
                                findNavController().navigate(R.id.actionToHome)
                            }
                        }
                    }

                    is NetworkRequest.Error -> {
                        loading.showVisibility(false,btnSubmit)
                        root.showSnackBar( response.message!!,
                            ContextCompat.getColor(requireContext(),R.color.philippineSilver))
                    }
                }

            }

            //Email
            emailEdt.addTextChangedListener {
                if (it.toString().contains('@')){
                    userEmail=it.toString()
                    emailEdt.error=""
                }else{
                    emailEdt.error= getString(R.string.email_is_not_valid)
                }
            }




            btnSubmit.setOnClickListener {
                val name=nameEdt.text.toString()
                val email=emailEdt.text.toString()
                val password=passEdt.text.toString()

                if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                    username=name
                    userEmail=email
                    userPassword=password
                    viewModel.sendUserData(username,userEmail,userPassword)
                }else{
                    it.showSnackBar(getString(R.string.please_fill_all_fields),
                        ContextCompat.getColor(requireContext(),R.color.scarlet))
                }

            }
        }
    }





    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
