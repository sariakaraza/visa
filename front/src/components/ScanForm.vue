<script setup>

import { ref } from 'vue'
import { defineEmits } from 'vue'

const emit = defineEmits(['data-loaded'])

const search = ref('')

const isPending = ref(false)
const errorMessage = ref('')
const demandePasseportData = ref(null)

const validerRecherche = async () => {
    isPending.value = true
    errorMessage.value = ''
    demandePasseportData.value = null
    try {
        const baseURL = import.meta.env.VITE_API_BASE_URL
        const urlComplete = `${baseURL}/api/demandes/history?numDemande=${search.value}&numPasseport=${search.value}`
        const response = await fetch(urlComplete)
        if (!response.ok) {
            throw new Error('Demande / Passeport introuvable ou erreur serveur')
        }
        const data = await response.json()
        demandePasseportData.value = data
        console.log(data)
        console.log(urlComplete)
        console.log(search.value)
        
        // Émettre l'événement avec les données
        emit('data-loaded', data)
    } catch (error) {
        errorMessage.value = error.message
    } finally {
        isPending.value = false
    }
}

</script>

<template>
  <div>
    <h2>Scan de la demande</h2>

    <form @submit.prevent="validerRecherche">
      <label for="numero">
        Numéro Passeport / Demande
      </label>

      <input
        v-model="search"
        type="text"
        id="numero"
        placeholder="Entrez le numéro..."
      />

      <button type="submit">
        Valider
      </button>
    </form>

    <p v-if="isPending">Chargement en cours...</p>
    <p v-if="errorMessage" style="color: red">{{ errorMessage }}</p>
  </div>
</template>