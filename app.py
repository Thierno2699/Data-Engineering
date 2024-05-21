import os
import json
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import streamlit as st

def find_json_files(directory):
    """ Fonction pour trouver tous les fichiers JSON dans les sous-dossiers. """
    json_files = []
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith('.json'):
                json_files.append(os.path.join(root, file))
    return json_files

def load_json_data(file_path):
    """ Fonction pour charger et parser un fichier JSON. """
    data = []
    with open(file_path, 'r') as file:
        for line in file:
            try:
                json_data = json.loads(line.strip())
                data.append(json_data)
            except json.JSONDecodeError as e:
                st.error(f"Erreur de décodage JSON dans le fichier {file_path}: {e}")
    return data

def main():
    data_folder = '/Users/thierno/Downloads/DataIng/data'
    json_files = find_json_files(data_folder)
    all_data = []

    for file in json_files:
        data = load_json_data(file)
        all_data.extend(data)
    
    if not all_data:
        st.error("Aucune donnée valide trouvée dans les fichiers JSON.")
        return

    df = pd.DataFrame(all_data)

    # Convertir les heures en format datetime et extraire l'heure seulement
    df['heure'] = pd.to_datetime(df['heure'], format='%H:%M').dt.hour

    # Graphique des alertes par heure
    st.header('Alertes par heure')
    plt.figure(figsize=(10, 4))
    alert_counts = df[df['alerte'] == True]['heure'].value_counts().sort_index()
    plt.bar(alert_counts.index, alert_counts.values, width=0.5, color='skyblue')
    plt.xticks(range(24))  # Afficher toutes les heures de 0 à 23
    plt.xlabel('Heure')
    plt.ylabel('Nombre d\'alertes')
    plt.title('Nombre d\'alertes par heure')
    st.pyplot(plt)
    
    # Carte des positions géographiques avec alerte == True
    st.header('Position géographique des alertes')
    st.map(df[df['alerte'] == True][['latitude', 'longitude']])


    # Graphique de la fréquence cardiaque vs. température ambiante
    st.header('Fréquence cardiaque vs. Température ambiante')
    plt.figure(figsize=(10, 4))
    plt.scatter(df[df['alerte'] == True]['temperature'], df[df['alerte'] == True]['frequence_cardiaque'], c='red', label='Alerte True')
    plt.scatter(df[df['alerte'] == False]['temperature'], df[df['alerte'] == False]['frequence_cardiaque'], c='blue', label='Alerte False')
    plt.xlabel('Température ambiante')
    plt.ylabel('Fréquence cardiaque')
    plt.legend()
    plt.title('Fréquence cardiaque vs. Température ambiante')
    st.pyplot(plt)

    # Graphique de la température corporelle vs. température ambiante
    st.header('Température corporelle vs. Température ambiante')
    plt.figure(figsize=(10, 4))
    plt.scatter(df[df['alerte'] == True]['temperature'], df[df['alerte'] == True]['temperature_corporelle'], c='red', label='Alerte True')
    plt.scatter(df[df['alerte'] == False]['temperature'], df[df['alerte'] == False]['temperature_corporelle'], c='blue', label='Alerte False')
    plt.xlabel('Température ambiante')
    plt.ylabel('Température corporelle')
    plt.legend()
    plt.title('Température corporelle vs. Température ambiante')
    st.pyplot(plt)

    # Histogramme de la fréquence cardiaque
    st.header('Histogramme de la fréquence cardiaque')
    plt.figure(figsize=(10, 4))
    plt.hist(df[df['alerte'] == True]['frequence_cardiaque'], bins=20, color='red', alpha=0.5, label='Alerte True')
    plt.hist(df[df['alerte'] == False]['frequence_cardiaque'], bins=20, color='blue', alpha=0.5, label='Alerte False')
    plt.xlabel('Fréquence cardiaque')
    plt.ylabel('Nombre d\'occurrences')
    plt.legend()
    plt.title('Distribution de la fréquence cardiaque')
    st.pyplot(plt)

    # Graphique en camembert pour visualiser les True vs False
    st.header('Proportion de True vs False dans les alertes')
    counts = df['alerte'].value_counts()
    plt.figure(figsize=(6, 6))
    plt.pie(counts, labels=['False', 'True'], autopct='%1.1f%%', colors=['lightcoral', 'lightgreen'])
    plt.title('Proportion de True vs False dans les alertes')
    plt.axis('equal')  # Assure que le graphique est circulaire
    st.pyplot(plt)

if __name__ == "__main__":
    st.title('Visualiseur de fichiers JSON')
    main()
