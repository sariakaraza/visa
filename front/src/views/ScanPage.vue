<script setup>
import { ref } from 'vue'
import ScanForm from '../components/ScanForm.vue'
import StatusTimeline from '../components/StatusTimeline.vue'

const demandeData = ref(null)

const handleDataLoaded = (data) => {
  demandeData.value = data
}

const openPublicPage = () => {
  if (demandeData.value && demandeData.value.numeroDemande) {
    const url = `http://localhost:5173/scan?numDemande=${demandeData.value.numeroDemande}`
    window.open(url, '_blank')
  }
}
</script>

<template>
  <div class="scan-page">
    <h1>Scanner une demande</h1>
    <ScanForm @data-loaded="handleDataLoaded" />
    
    <div v-if="demandeData" class="demande-info">
      <h2>Informations de la demande</h2>
      <p><strong>Numéro recherché :</strong> {{ demandeData.numeroRecherche }} ({{ demandeData.typeRecherche }})</p>
      <p><strong>ID :</strong> {{ demandeData.idDemande }}</p>
      <p><strong>Numéro de demande :</strong> {{ demandeData.numeroDemande }}</p>
      <p><strong>Demandeur :</strong> {{ demandeData.nomDemandeur }}</p>
      <p><strong>Date de création :</strong> {{ new Date(demandeData.dateCreation).toLocaleDateString('fr-FR') }}</p>
      
      <StatusTimeline :status-history="demandeData.statusHistory" />
      
      <!-- <div v-if="demandeData.cheminQr" class="qr-section">
        <button @click="openPublicPage" class="open-page-btn">
          Ouvrir la page publique
        </button>
      </div> -->
    </div>
  </div>
</template>

<script>
export default {
  name: 'ScanPage'
}
</script>

<style scoped>
.scan-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.demande-info {
  margin-top: 30px;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #dee2e6;
}

.demande-info h2 {
  margin-top: 0;
  color: #333;
}

.demande-info p {
  margin: 10px 0;
}

.qr-section {
  margin-top: 20px;
}

.open-page-btn {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1em;
}

.open-page-btn:hover {
  background-color: #218838;
}
</style>
