import { IActividad } from 'app/entities/actividad/actividad.model';
import { TipoIdentificacion } from 'app/entities/enumerations/tipo-identificacion.model';
import dayjs from 'dayjs/esm';

export interface IEmpleado {
  id?: number;
  nombres?: string | null;
  apellidos?: string | null;
  fechaNacimiento?: dayjs.Dayjs | null;
  tipoIdentificacion?: TipoIdentificacion | null;
  numeroIdentificacion?: string | null;
  actividads?: IActividad[] | null;
}

export class Empleado implements IEmpleado {
  constructor(
    public id?: number,
    public nombres?: string | null,
    public apellidos?: string | null,
    public fechaNacimiento?: dayjs.Dayjs | null,
    public tipoIdentificacion?: TipoIdentificacion | null,
    public numeroIdentificacion?: string | null,
    public actividads?: IActividad[] | null
  ) {}
}

export function getEmpleadoIdentifier(empleado: IEmpleado): number | undefined {
  return empleado.id;
}
