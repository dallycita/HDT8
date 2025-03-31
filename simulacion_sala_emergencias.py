import simpy
import random
import statistics
import matplotlib.pyplot as plt

# ESCENARIOS QUE VAMOS A SIMULAR
ESCENARIOS = {
    "Día Normal": {
        "pacientes": 200,
        "llegada_prom": 5,
        "enfermeras": 2,
        "doctores": 4,
        "labs": 2,
        "rayosx": 2
    },
    "Fin de Semana": {
        "pacientes": 150,
        "llegada_prom": 7,
        "enfermeras": 2,
        "doctores": 3,
        "labs": 2,
        "rayosx": 2
    },
    "Fecha Festiva": {
        "pacientes": 250,
        "llegada_prom": 3,
        "enfermeras": 3,
        "doctores": 5,
        "labs": 2,
        "rayosx": 2
    }
}

# Tiempos aproximados por área (en minutos)
TIEMPO_EN_TRIAGE = 10
TIEMPO_CON_DOCTOR = 20
TIEMPO_EN_LAB = 15
TIEMPO_EN_RAYOSX = 15

# DECISIONES CLÍNICAS SEGÚN PRIORIDAD

def necesita_laboratorio(prioridad):
    return prioridad in [1, 2, 3]

def necesita_rayosx(prioridad):
    return prioridad in [1, 2, 4]

# ÁREAS DE ATENCIÓN
def ir_a_triage(env, prioridad, enfermeras):
    with enfermeras.request(priority=prioridad) as req:
        inicio = env.now
        yield req
        yield env.timeout(TIEMPO_EN_TRIAGE)
        return env.now - inicio

def ver_al_doctor(env, prioridad, doctores):
    with doctores.request(priority=prioridad) as req:
        inicio = env.now
        yield req
        yield env.timeout(TIEMPO_CON_DOCTOR)
        return env.now - inicio

def ir_a_laboratorio(env, prioridad, laboratorios):
    with laboratorios.request(priority=prioridad) as req:
        inicio = env.now
        yield req
        yield env.timeout(TIEMPO_EN_LAB)
        return env.now - inicio

def ir_a_rayosx(env, prioridad, rayosx):
    with rayosx.request(priority=prioridad) as req:
        inicio = env.now
        yield req
        yield env.timeout(TIEMPO_EN_RAYOSX)
        return env.now - inicio


# FLUJO COMPLETO DE UN PACIENTE

def atender_paciente(env, id, enfermeras, doctores, laboratorios, rayosx, tiempos):
    llegada = env.now
    prioridad = random.randint(1, 5)

    espera_enf = yield env.process(ir_a_triage(env, prioridad, enfermeras))
    espera_doc = yield env.process(ver_al_doctor(env, prioridad, doctores))

    espera_lab = 0
    if necesita_laboratorio(prioridad):
        espera_lab = yield env.process(ir_a_laboratorio(env, prioridad, laboratorios))

    espera_rx = 0
    if necesita_rayosx(prioridad):
        espera_rx = yield env.process(ir_a_rayosx(env, prioridad, rayosx))

    salida = env.now
    total = salida - llegada

    tiempos['total'].append(total)
    tiempos['triage'].append(espera_enf)
    tiempos['doctor'].append(espera_doc)
    tiempos['lab'].append(espera_lab)
    tiempos['rayosx'].append(espera_rx)

# LOS PACIENTES VAN LLEGANDO
def flujo_de_pacientes(env, config, enfermeras, doctores, laboratorios, rayosx, tiempos):
    for i in range(config["pacientes"]):
        env.process(atender_paciente(env, i, enfermeras, doctores, laboratorios, rayosx, tiempos))
        siguiente = random.expovariate(1.0 / config["llegada_prom"])
        yield env.timeout(siguiente)


# FUNCIÓN QUE CORRE LA SIMULACIÓN

def correr_simulacion(nombre, config):
    random.seed(42)
    env = simpy.Environment()

    enfermeras = simpy.PriorityResource(env, capacity=config["enfermeras"])
    doctores = simpy.PriorityResource(env, capacity=config["doctores"])
    laboratorios = simpy.PriorityResource(env, capacity=config["labs"])
    rayosx = simpy.PriorityResource(env, capacity=config["rayosx"])

    tiempos = {'total': [], 'triage': [], 'doctor': [], 'lab': [], 'rayosx': []}
    env.process(flujo_de_pacientes(env, config, enfermeras, doctores, laboratorios, rayosx, tiempos))
    env.run()

    print(f"\nResultados de la simulación: {nombre}")
    print(f"Pacientes atendidos: {config['pacientes']}")
    print(f"Tiempo total promedio: {round(statistics.mean(tiempos['total']), 2)} min")
    print(f"Espera en triage: {round(statistics.mean(tiempos['triage']), 2)} min")
    print(f"Espera para ver al doctor: {round(statistics.mean(tiempos['doctor']), 2)} min")
    print(f"Espera en laboratorio: {round(statistics.mean(tiempos['lab']), 2)} min")
    print(f"Espera en rayos X: {round(statistics.mean(tiempos['rayosx']), 2)} min")

    plt.hist(tiempos['total'], bins=20, edgecolor='black')
    plt.title(f"Distribución de tiempo total - {nombre}")
    plt.xlabel("Tiempo total (min)")
    plt.ylabel("Cantidad de pacientes")
    plt.grid(True)
    plt.tight_layout()
    plt.show()


# CORREMOS LAS SIMULACIONES
for nombre, config in ESCENARIOS.items():
    correr_simulacion(nombre, config)
