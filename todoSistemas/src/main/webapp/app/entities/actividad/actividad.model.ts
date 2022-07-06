import dayjs from 'dayjs/esm';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EstadoActividad } from 'app/entities/enumerations/estado-actividad.model';

export interface IActividad {
  id?: number;
  nombre?: string | null;
  fechaEstimadaEjecucion?: dayjs.Dayjs | null;
  descripcion?: string | null;
  estado?: EstadoActividad | null;
  empleado?: IEmpleado | null;
  diasRetraso?: string | null;
}

export class Actividad implements IActividad {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public fechaEstimadaEjecucion?: dayjs.Dayjs | null,
    public descripcion?: string | null,
    public estado?: EstadoActividad | null,
    public empleado?: IEmpleado | null,
    public diasRetraso?: string | null
  ) {}
}

export function getActividadIdentifier(actividad: IActividad): number | undefined {
  return actividad.id;
}
